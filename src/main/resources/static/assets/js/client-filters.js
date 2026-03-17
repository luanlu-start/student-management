(function () {
    function byId(id) {
        return document.getElementById(id);
    }

    function initSemesterFilter() {
        var searchInput = byId("semesterSearchInput");
        var statusFilter = byId("semesterStatusFilter");
        var tableBody = byId("semesterTableBody");
        if (!searchInput || !statusFilter || !tableBody) return;

        var rows = Array.prototype.slice.call(tableBody.querySelectorAll("tr[data-code]"));
        var noResultRow = byId("semesterNoResultRow");

        function getStatus(endDateText) {
            var endDate = new Date(endDateText + "T00:00:00");
            if (Number.isNaN(endDate.getTime())) return "disabled";
            var today = new Date();
            today.setHours(0, 0, 0, 0);
            return endDate >= today ? "active" : "disabled";
        }

        function applyFilter() {
            var keyword = searchInput.value.trim().toLowerCase();
            var status = statusFilter.value;
            var visibleCount = 0;

            rows.forEach(function (row) {
                var code = (row.dataset.code || "").toLowerCase();
                var year = (row.dataset.year || "").toLowerCase();
                var begin = (row.dataset.begin || "").toLowerCase();
                var end = (row.dataset.end || "").toLowerCase();
                var rowText = code + " " + year + " " + begin + " " + end;

                var matchKeyword = keyword === "" || rowText.indexOf(keyword) !== -1;
                var matchStatus = status === "" || getStatus(row.dataset.end || "") === status;
                var visible = matchKeyword && matchStatus;

                row.style.display = visible ? "" : "none";
                if (visible) visibleCount += 1;
            });

            if (noResultRow) noResultRow.style.display = visibleCount === 0 ? "" : "none";
        }

        searchInput.addEventListener("input", applyFilter);
        statusFilter.addEventListener("change", applyFilter);
    }

    function initCardFilter(config) {
        var searchInput = byId(config.searchId);
        var departmentFilter = byId(config.departmentId);
        var resetButton = byId(config.resetId);
        var cards = Array.prototype.slice.call(document.querySelectorAll(config.cardSelector));
        var noResult = byId(config.noResultId);
        var pagination = document.querySelector(config.paginationSelector || "");

        if (!searchInput || !departmentFilter || cards.length === 0) return;

        function applyFilter() {
            var keyword = searchInput.value.trim().toLowerCase();
            var dep = departmentFilter.value;
            var visibleCount = 0;
            var isFiltering = keyword !== "" || dep !== "all";

            cards.forEach(function (card) {
                var name = (card.dataset.name || "").toLowerCase();
                var department = (card.dataset.department || "none").toLowerCase();

                var matchName = keyword === "" || name.indexOf(keyword) !== -1;
                var matchDepartment = dep === "all" || department === dep.toLowerCase();
                var visible = matchName && matchDepartment;

                card.style.display = visible ? "" : "none";
                if (visible) visibleCount += 1;
            });

            if (noResult) noResult.style.display = visibleCount === 0 ? "" : "none";
            if (pagination) pagination.style.display = isFiltering ? "none" : "";
        }

        searchInput.addEventListener("input", applyFilter);
        departmentFilter.addEventListener("change", applyFilter);
        if (resetButton) {
            resetButton.addEventListener("click", function () {
                searchInput.value = "";
                departmentFilter.value = "all";
                applyFilter();
            });
        }
    }

    function initCourseFilter() {
        var searchInput = byId("courseSearchInput");
        var departmentFilter = byId("courseDepartmentFilter");
        var resetButton = byId("courseFilterReset");
        var tableBody = byId("courseTableBody");
        if (!searchInput || !departmentFilter || !tableBody) return;

        var rows = Array.prototype.slice.call(tableBody.querySelectorAll("tr[data-code]"));
        var noResultRow = byId("courseNoResultRow");

        function applyFilter() {
            var keyword = searchInput.value.trim().toLowerCase();
            var dep = departmentFilter.value;
            var visibleCount = 0;

            rows.forEach(function (row) {
                var code = (row.dataset.code || "").toLowerCase();
                var title = (row.dataset.title || "").toLowerCase();
                var department = (row.dataset.department || "none").toLowerCase();

                var matchKeyword = keyword === "" || code.indexOf(keyword) !== -1 || title.indexOf(keyword) !== -1;
                var matchDepartment = dep === "all" || dep.toLowerCase() === department;
                var visible = matchKeyword && matchDepartment;

                row.style.display = visible ? "" : "none";
                if (visible) visibleCount += 1;
            });

            if (noResultRow) noResultRow.style.display = visibleCount === 0 ? "" : "none";
        }

        searchInput.addEventListener("input", applyFilter);
        departmentFilter.addEventListener("change", applyFilter);
        if (resetButton) {
            resetButton.addEventListener("click", function () {
                searchInput.value = "";
                departmentFilter.value = "all";
                applyFilter();
            });
        }
    }

    function initUserFilter() {
        var searchInput = byId("userSearchInput");
        var roleFilter = byId("userRoleFilter");
        var departmentFilter = byId("userDepartmentFilter");
        var cards = Array.prototype.slice.call(document.querySelectorAll("#userGrid .user-card"));
        var noResult = byId("userNoResult");

        if (!searchInput || !roleFilter || !departmentFilter || cards.length === 0) return;

        var departments = {};
        cards.forEach(function (card) {
            var dep = (card.dataset.department || "").trim();
            if (dep !== "") departments[dep] = true;
        });

        Object.keys(departments).sort().forEach(function (dep) {
            var option = document.createElement("option");
            option.value = dep.toLowerCase();
            option.textContent = dep;
            departmentFilter.appendChild(option);
        });

        function applyFilter() {
            var keyword = searchInput.value.trim().toLowerCase();
            var role = roleFilter.value;
            var dep = departmentFilter.value;
            var visibleCount = 0;

            cards.forEach(function (card) {
                var username = (card.dataset.username || "").toLowerCase();
                var cardRole = (card.dataset.role || "").toLowerCase();
                var cardDep = (card.dataset.department || "").toLowerCase();
                var fullText = username + " " + cardRole + " " + cardDep + " " + card.textContent.toLowerCase();

                var matchKeyword = keyword === "" || fullText.indexOf(keyword) !== -1;
                var matchRole = role === "" || cardRole === role;
                var matchDep = dep === "" || cardDep === dep;
                var visible = matchKeyword && matchRole && matchDep;

                card.style.display = visible ? "" : "none";
                if (visible) visibleCount += 1;
            });

            if (noResult) noResult.style.display = visibleCount === 0 ? "" : "none";
        }

        searchInput.addEventListener("input", applyFilter);
        roleFilter.addEventListener("change", applyFilter);
        departmentFilter.addEventListener("change", applyFilter);
    }

    function initAssessmentFilter() {
        var searchInput = byId("searchInput");
        var accordions = Array.prototype.slice.call(document.querySelectorAll(".course-accordion"));
        if (!searchInput || accordions.length === 0 || !document.querySelector(".course-header")) return;

        accordions.forEach(function (accordion) {
            var header = accordion.querySelector(".course-header");
            if (header) {
                header.addEventListener("click", function () {
                    accordion.classList.toggle("active");
                });
            }

            var addBtn = accordion.querySelector(".btn-add-assessment");
            if (addBtn) {
                addBtn.addEventListener("click", function (event) {
                    event.stopPropagation();
                });
            }
        });

        searchInput.addEventListener("input", function (e) {
            var searchTerm = e.target.value.toLowerCase();
            accordions.forEach(function (accordion) {
                var courseName = (accordion.getAttribute("data-course-name") || "").toLowerCase();
                var titleEl = accordion.querySelector(".course-details h4");
                var courseCode = titleEl ? titleEl.textContent.toLowerCase() : "";
                accordion.style.display = (courseName.indexOf(searchTerm) !== -1 || courseCode.indexOf(searchTerm) !== -1) ? "block" : "none";
            });
        });
    }

    function initClassFilter() {
        var searchInput = byId("searchInput");
        var semesterFilter = byId("semesterFilter");
        var lecturerFilter = byId("lecturerFilter");
        var resetButton = byId("classFilterReset");
        var classRows = Array.prototype.slice.call(document.querySelectorAll(".class-row"));
        var displayedCount = byId("displayedCount");
        var noResultsMessage = byId("noResultsMessage");

        if (!searchInput || !semesterFilter || !lecturerFilter || classRows.length === 0) return;

        function filterClasses() {
            var searchTerm = searchInput.value.toLowerCase().trim();
            var selectedSemester = semesterFilter.value.toLowerCase();
            var selectedLecturer = lecturerFilter.value.toLowerCase();
            var visibleCount = 0;

            classRows.forEach(function (row) {
                var course = (row.getAttribute("data-course") || "").toLowerCase();
                var code = (row.getAttribute("data-code") || "").toLowerCase();
                var lecturer = (row.getAttribute("data-lecturer") || "").toLowerCase();
                var semester = (row.getAttribute("data-semester") || "").toLowerCase();
                var department = (row.getAttribute("data-department") || "").toLowerCase();

                var matchSearch = searchTerm === "" ||
                    course.indexOf(searchTerm) !== -1 ||
                    code.indexOf(searchTerm) !== -1 ||
                    lecturer.indexOf(searchTerm) !== -1 ||
                    department.indexOf(searchTerm) !== -1;

                var matchSemester = selectedSemester === "" || semester === selectedSemester;
                var matchLecturer = selectedLecturer === "" || lecturer === selectedLecturer;
                var visible = matchSearch && matchSemester && matchLecturer;

                row.style.display = visible ? "" : "none";
                if (visible) visibleCount += 1;
            });

            if (displayedCount) displayedCount.textContent = String(visibleCount);
            if (noResultsMessage) noResultsMessage.style.display = visibleCount === 0 ? "block" : "none";
        }

        searchInput.addEventListener("input", filterClasses);
        semesterFilter.addEventListener("change", filterClasses);
        lecturerFilter.addEventListener("change", filterClasses);

        if (resetButton) {
            resetButton.addEventListener("click", function () {
                searchInput.value = "";
                semesterFilter.value = "";
                lecturerFilter.value = "";
                filterClasses();
            });
        }
    }

    function initMarkFilter() {
        var tableBody = byId("markTableBody");
        var searchInput = byId("searchInput");
        var courseFilter = byId("courseFilter");
        var assessmentFilter = byId("assessmentFilter");
        var resetButton = byId("markFilterReset");
        var displayedCount = byId("displayedCount");
        var noResultsMessage = byId("noResultsMessage");
        if (!tableBody || !searchInput || !courseFilter || !assessmentFilter) return;

        var rows = Array.prototype.slice.call(tableBody.querySelectorAll(".mark-row"));
        var table = tableBody.closest("table");

        function applyFilter() {
            var searchTerm = searchInput.value.toLowerCase().trim();
            var selectedCourse = courseFilter.value.toLowerCase();
            var selectedAssessment = assessmentFilter.value.toLowerCase();
            var visibleCount = 0;

            rows.forEach(function (row) {
                var student = (row.getAttribute("data-student") || "").toLowerCase();
                var course = (row.getAttribute("data-course") || "").toLowerCase();
                var courseCode = (row.getAttribute("data-course-code") || "").toLowerCase();
                var assessment = (row.getAttribute("data-assessment") || "").toLowerCase();

                var matchSearch = searchTerm === "" ||
                    student.indexOf(searchTerm) !== -1 ||
                    course.indexOf(searchTerm) !== -1 ||
                    courseCode.indexOf(searchTerm) !== -1;
                var matchCourse = selectedCourse === "" || course === selectedCourse;
                var matchAssessment = selectedAssessment === "" || assessment === selectedAssessment;
                var visible = matchSearch && matchCourse && matchAssessment;

                row.style.display = visible ? "" : "none";
                if (visible) visibleCount += 1;
            });

            if (displayedCount) displayedCount.textContent = String(visibleCount);
            if (noResultsMessage) noResultsMessage.style.display = visibleCount === 0 ? "block" : "none";
            if (table) table.style.display = visibleCount === 0 ? "none" : "table";
        }

        searchInput.addEventListener("input", applyFilter);
        courseFilter.addEventListener("change", applyFilter);
        assessmentFilter.addEventListener("change", applyFilter);

        if (resetButton) {
            resetButton.addEventListener("click", function () {
                searchInput.value = "";
                courseFilter.value = "";
                assessmentFilter.value = "";
                applyFilter();
            });
        }
    }

    function initViewClassStudentsFilter() {
        var searchInput = byId("searchStudent");
        var studentRows = Array.prototype.slice.call(document.querySelectorAll(".student-row"));
        var studentCount = byId("studentCount");
        var noResultsMessage = byId("noResultsMessage");
        if (!searchInput || studentRows.length === 0 || !studentCount) return;

        var totalStudents = studentRows.length;

        searchInput.addEventListener("input", function () {
            var searchTerm = searchInput.value.toLowerCase().trim();
            var visibleCount = 0;

            studentRows.forEach(function (row) {
                var name = (row.getAttribute("data-name") || "").toLowerCase();
                var email = (row.getAttribute("data-email") || "").toLowerCase();
                var phone = (row.getAttribute("data-phone") || "").toLowerCase();
                var department = (row.getAttribute("data-department") || "").toLowerCase();

                var visible = name.indexOf(searchTerm) !== -1 ||
                    email.indexOf(searchTerm) !== -1 ||
                    phone.indexOf(searchTerm) !== -1 ||
                    department.indexOf(searchTerm) !== -1;

                row.style.display = visible ? "" : "none";
                if (visible) visibleCount += 1;
            });

            studentCount.textContent = String(searchTerm === "" ? totalStudents : visibleCount);
            if (noResultsMessage) {
                noResultsMessage.style.display = visibleCount === 0 && searchTerm !== "" ? "block" : "none";
            }
        });
    }

    function initViewClassMarksFilter() {
        var searchInput = byId("gradebookSearch");
        var tableBody = byId("gradebookTableBody");
        var noResultsRow = byId("noResultsRow");
        if (!searchInput || !tableBody) return;

        var rows = Array.prototype.slice.call(tableBody.querySelectorAll("tr.grade-row"));

        searchInput.addEventListener("input", function () {
            var searchTerm = searchInput.value.toLowerCase().trim();
            var visibleCount = 0;

            rows.forEach(function (row) {
                var name = (row.getAttribute("data-name") || "").toLowerCase();
                var visible = name.indexOf(searchTerm) !== -1;
                row.style.display = visible ? "" : "none";
                if (visible) visibleCount += 1;
            });

            if (noResultsRow) {
                noResultsRow.style.display = (visibleCount === 0 && searchTerm !== "") ? "table-row" : "none";
            }
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        initSemesterFilter();
        initCardFilter({
            searchId: "studentSearchInput",
            departmentId: "studentDepartmentFilter",
            resetId: "studentFilterReset",
            cardSelector: "#studentGrid .user-card",
            noResultId: "studentNoResult",
            paginationSelector: "nav.pag"
        });
        initCardFilter({
            searchId: "lecturerSearchInput",
            departmentId: "lecturerDepartmentFilter",
            resetId: "lecturerFilterReset",
            cardSelector: "#lecturerGrid .user-card",
            noResultId: "lecturerNoResult",
            paginationSelector: "nav.pag"
        });
        initCourseFilter();
        initUserFilter();
        initAssessmentFilter();
        initClassFilter();
        initMarkFilter();
        initViewClassStudentsFilter();
        initViewClassMarksFilter();
    });
})();
