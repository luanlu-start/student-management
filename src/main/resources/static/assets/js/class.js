const searchInput = document.getElementById('searchInput');
const semesterFilter = document.getElementById('semesterFilter');
const lecturerFilter = document.getElementById('lecturerFilter');
const classRows = document.querySelectorAll('.class-row');
const displayedCount = document.getElementById('displayedCount');
const noResultsMessage = document.getElementById('noResultsMessage');
const totalClasses = /*[[${totalClasses}]]*/ 0;

function filterClasses() {
    const searchTerm = searchInput.value.toLowerCase().trim();
    const selectedSemester = semesterFilter.value.toLowerCase();
    const selectedLecturer = lecturerFilter.value.toLowerCase();
    let visibleCount = 0;

    classRows.forEach(row => {
        const course = (row.getAttribute('data-course') || '').toLowerCase();
        const code = (row.getAttribute('data-code') || '').toLowerCase();
        const lecturer = (row.getAttribute('data-lecturer') || '').toLowerCase();
        const semester = (row.getAttribute('data-semester') || '').toLowerCase();
        const department = (row.getAttribute('data-department') || '').toLowerCase();

        const matchSearch = searchTerm === '' ||
            course.includes(searchTerm) ||
            code.includes(searchTerm) ||
            lecturer.includes(searchTerm) ||
            department.includes(searchTerm);

        const matchSemester = selectedSemester === '' ||
            semester === selectedSemester;

        const matchLecturer = selectedLecturer === '' ||
            lecturer === selectedLecturer;

        if (matchSearch && matchSemester && matchLecturer) {
            row.style.display = '';
            visibleCount++;
        } else {
            row.style.display = 'none';
        }
    });

    displayedCount.textContent = visibleCount;
    noResultsMessage.style.display = visibleCount === 0 ? 'block' : 'none';
}

function resetFilters() {
    searchInput.value = '';
    semesterFilter.value = '';
    lecturerFilter.value = '';
    filterClasses();
    displayedCount.textContent = totalClasses;
    noResultsMessage.style.display = 'none';
}

searchInput.addEventListener('input', filterClasses);
semesterFilter.addEventListener('change', filterClasses);
lecturerFilter.addEventListener('change', filterClasses);