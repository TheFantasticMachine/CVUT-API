const dialogEl = document.getElementById('settings-popup-wrapper');
const openSettingsBtn = document.getElementById('btn-settings');
const closeSettingsBtn = dialogEl.querySelector('.btn-close');

// Open close events
openSettingsBtn.addEventListener('click', (e) => {dialogEl.showModal()})
closeSettingsBtn.addEventListener('click', (e) => {dialogEl.close()})
dialogEl.addEventListener("click", (e) => {
    if (!document.querySelector('.excluded').contains(e.target)) {
        dialogEl.close();
    }
});