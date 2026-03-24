/* * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener("DOMContentLoaded", () => {
    console.log("✅ dashboard.js loaded");

    // --- SIDEBAR DROPDOWN ---
    const allDropdown = document.querySelectorAll('#sidebar .side-dropdown');
    const sidebar = document.getElementById('sidebar');

    if (allDropdown && allDropdown.length) {
        allDropdown.forEach(item => {
            const a = item.parentElement.querySelector('a:first-child');
            if (!a) return;
            a.addEventListener('click', function (e) {
                e.preventDefault();

                if (!this.classList.contains('active')) {
                    allDropdown.forEach(i => {
                        const aLink = i.parentElement.querySelector('a:first-child');
                        if (aLink) aLink.classList.remove('active');
                        i.classList.remove('show');
                    });
                }

                this.classList.toggle('active');
                item.classList.toggle('show');
            });
        });
    }

    // --- SIDEBAR COLLAPSE ---
    const toggleSidebar = document.querySelector('nav .toggle-sidebar');
    const allSideDivider = document.querySelectorAll('#sidebar .divider');

    function checkSidebarHide() {
        if (!sidebar) return;
        if (sidebar.classList.contains('hide')) {
            allSideDivider.forEach(item => item.textContent = '-');
            allDropdown.forEach(item => {
                const a = item.parentElement.querySelector('a:first-child');
                if (a) a.classList.remove('active');
                item.classList.remove('show');
            });
        } else {
            allSideDivider.forEach(item => item.textContent = item.dataset.text);
        }
    }
    checkSidebarHide();

    if (toggleSidebar) {
        toggleSidebar.addEventListener('click', function () {
            if (!sidebar) return;
            sidebar.classList.toggle('hide');
            checkSidebarHide();
        });
    }

    if (sidebar) {
        sidebar.addEventListener('mouseleave', function () {
            if (this.classList.contains('hide')) {
                allDropdown.forEach(item => {
                    const a = item.parentElement.querySelector('a:first-child');
                    if (a) a.classList.remove('active');
                    item.classList.remove('show');
                });
                allSideDivider.forEach(item => item.textContent = '-');
            }
        });

        sidebar.addEventListener('mouseenter', function () {
            if (this.classList.contains('hide')) {
                allDropdown.forEach(item => {
                    const a = item.parentElement.querySelector('a:first-child');
                    if (a) a.classList.remove('active');
                    item.classList.remove('show');
                });
                allSideDivider.forEach(item => item.textContent = item.dataset.text);
            }
        });
    }

    // --- PROFILE DROPDOWN ---
    const profile = document.querySelector('nav .profile');
    const dropdownProfile = profile ? profile.querySelector('.profile-link') : null;

    if (profile && dropdownProfile) {
        profile.addEventListener('click', function (e) {
            e.stopPropagation();
            dropdownProfile.classList.toggle('show');
        });

        document.addEventListener('click', function (e) {
            if (!profile.contains(e.target)) {
                dropdownProfile.classList.remove('show');
            }
        });
    } else {
        console.warn("Not found .profile or .profile-link trong DOM");
    }


    function toggleLecturerSelect() {
        const roleSelect = document.getElementById('roleSelect');
        const lecturerGroup = document.getElementById('lecturerFormGroup');
        const lecturerSelect = document.getElementById('lecturerSelect');
        const naOption = document.getElementById('na_option');

        if (!roleSelect || !lecturerGroup || !lecturerSelect || !naOption) {
            return;
        }

        if (roleSelect.value === 'admin') {
            lecturerGroup.style.display = 'none';
            lecturerSelect.required = false;
            lecturerSelect.value = '0';
            naOption.style.display = 'block';
        } else {
            lecturerGroup.style.display = 'block';
            lecturerSelect.required = true;
            naOption.style.display = 'none';

            if (lecturerSelect.value === '0') {
                lecturerSelect.value = '';
            }
        }
    }

    toggleLecturerSelect();

    const mainContent = document.getElementById('dashboard-main');
    if (mainContent) {
        mainContent.addEventListener('change', function(e) {
            if (e.target.id === 'roleSelect') {
                toggleLecturerSelect();
            }
        });
    }

    // Auto-hide flash alerts after 2 seconds for cleaner UX.
    const alerts = document.querySelectorAll('.alert');
    if (alerts.length) {
        alerts.forEach(function (alert) {
            setTimeout(function () {
                alert.style.transition = 'opacity 0.3s ease';
                alert.style.opacity = '0';
                setTimeout(function () {
                    if (alert.parentNode) {
                        alert.parentNode.removeChild(alert);
                    }
                }, 300);
            }, 2000);
        });
    }
});