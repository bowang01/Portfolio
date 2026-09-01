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
