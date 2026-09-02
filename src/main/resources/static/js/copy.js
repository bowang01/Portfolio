function copyText(value) {
    if (navigator.clipboard && window.isSecureContext) {
        return navigator.clipboard.writeText(value);
    }

    return new Promise((resolve, reject) => {
        const textarea = document.createElement("textarea");
        textarea.value = value;
        textarea.setAttribute("readonly", "");
        textarea.style.position = "fixed";
        textarea.style.top = "0";
        textarea.style.left = "-9999px";
        document.body.appendChild(textarea);
        textarea.focus();
        textarea.select();
        textarea.setSelectionRange(0, textarea.value.length);
        const ok = document.execCommand("copy");
        document.body.removeChild(textarea);
        if (ok) {
            resolve();
        } else {
            reject(new Error("copy failed"));
        }
    });
}

function flash(button, text) {
    const original = button.dataset.label || button.textContent;
    button.dataset.label = original;
    button.textContent = text;
    setTimeout(() => {
        button.textContent = original;
    }, 1200);
}

document.querySelectorAll(".copy").forEach((button) => {
    button.addEventListener("click", async () => {
        const value = button.getAttribute("data-copy") || "";
        try {
            await copyText(value);
            flash(button, "Copied");
        } catch {
            flash(button, "Copy failed");
        }
    });
});
