document.querySelectorAll("[data-add]").forEach((button) => {
    button.addEventListener("click", () => {
        const type = button.getAttribute("data-add");
        const wrap = document.getElementById(type);
        const template = document.getElementById(type === "accounts" ? "account-row" : "endpoint-row");
        if (!wrap || !template) return;
        const index = wrap.querySelectorAll(".repeat-row").length;
        wrap.insertAdjacentHTML("beforeend", template.innerHTML.replaceAll("INDEX", String(index)));
    });
});

const galleryInput = document.getElementById("gallery-files");
const galleryPending = document.getElementById("gallery-pending");
if (galleryInput && galleryPending) {
    const chosen = new DataTransfer();

    const renderPending = () => {
        galleryPending.replaceChildren();
        Array.from(chosen.files).forEach((file, index) => {
            const item = document.createElement("div");
            item.className = "gallery-admin-item";
            const img = document.createElement("img");
            img.alt = file.name;
            img.src = URL.createObjectURL(file);
            const remove = document.createElement("button");
            remove.type = "button";
            remove.className = "link-btn danger";
            remove.textContent = "Remove";
            remove.addEventListener("click", () => {
                const next = new DataTransfer();
                Array.from(chosen.files).forEach((kept, keptIndex) => {
                    if (keptIndex !== index) {
                        next.items.add(kept);
                    }
                });
                chosen.items.clear();
                Array.from(next.files).forEach((kept) => chosen.items.add(kept));
                galleryInput.files = chosen.files;
                renderPending();
            });
            item.append(img, remove);
            galleryPending.append(item);
        });
    };

    galleryInput.addEventListener("change", () => {
        Array.from(galleryInput.files).forEach((file) => {
            const exists = Array.from(chosen.files).some((kept) =>
                kept.name === file.name && kept.size === file.size && kept.lastModified === file.lastModified
            );
            if (!exists) {
                chosen.items.add(file);
            }
        });
        galleryInput.files = chosen.files;
        renderPending();
    });
}

document.addEventListener("click", (event) => {
    const target = event.target;
    if (!(target instanceof HTMLElement) || !target.hasAttribute("data-remove")) return;
    const row = target.closest(".repeat-row");
    const wrap = row?.parentElement;
    row?.remove();
    if (!wrap) return;
    wrap.querySelectorAll(".repeat-row").forEach((item, index) => {
        item.querySelectorAll("input").forEach((input) => {
            input.name = input.name.replace(/\[\d+]/, `[${index}]`);
        });
    });
});
