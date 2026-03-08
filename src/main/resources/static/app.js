const API = "http://localhost:8080/api";

async function loadStats() {
    const crsStats = await fetch(`${API}/courses/stats`).then(r => r.json());
    const stdStats = await fetch(`${API}/students/stats`).then(r => r.json());

    document.getElementById("statCourses").textContent = crsStats.totalCourses;
    document.getElementById("statStudents").textContent = stdStats.totalStudents;
    document.getElementById("statActive").textContent = stdStats.activeStudents;
    document.getElementById("statSeats").textContent = crsStats.totalSeats;
}

function showSection(name) {
    document.getElementById("courses").classList.add("hidden");
    document.getElementById("students").classList.add("hidden");
    document.getElementById(name).classList.remove("hidden");
    if (name === "courses") loadCourses();
    if (name === "students") { loadStudents(); loadCoursesDropdown(); }
}


function showToast(msg, type = "success") {
    const t = document.getElementById("toast");
    t.textContent = msg;
    t.className = `toast ${type}`;
    setTimeout(() => t.classList.add("hidden"), 3000);
}


function validateCourse() {
    const name = document.getElementById("crsName").value.trim();
    const dur = document.getElementById("crsDuration").value;
    const seats = document.getElementById("crsSeats").value;
    if (!name) { showToast("Course name is required", "error"); return false; }
    if (!dur || dur < 1) { showToast("Duration must be at least 1 week", "error"); return false; }
    if (!seats || seats < 0) { showToast("Seats cannot be negative", "error"); return false; }
    return true;
}

function validateStudent() {
    const name = document.getElementById("stdName").value.trim();
    const email = document.getElementById("stdEmail").value.trim();
    const crsId = document.getElementById("stdCourse").value;
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!name) { showToast("Student name is required", "error"); return false; }
    if (!email) { showToast("Email is required", "error"); return false; }
    if (!emailPattern.test(email)) { showToast("Enter a valid email", "error"); return false; }
    if (!crsId) { showToast("Course must be selected", "error"); return false; }
    return true;
}

async function loadCourses() {
    const res = await fetch(`${API}/courses`);
    const data = await res.json();
    const tbody = document.getElementById("crsBody");
    tbody.innerHTML = "";
    data.forEach(c => {
        tbody.innerHTML += `
        <tr>
            <td>${c.id}</td>
            <td>${c.crsName}</td>
            <td>${c.duration} weeks</td>
            <td>${c.instructorName || "-"}</td>
            <td>₹${c.crsFee}</td>
            <td>${c.availableSeats}</td>
            <td>
                <button class="btn-edit" onclick="editCourse(${c.id})">Edit</button>
                <button class="btn-delete" onclick="deleteCourse(${c.id})">Delete</button>
            </td>
        </tr>`;
    });

    loadStats();
}

function showCrsForm() {
    document.getElementById("crsFormTitle").textContent = "Add Course";
    document.getElementById("crsId").value = "";
    document.getElementById("crsName").value = "";
    document.getElementById("crsDuration").value = "";
    document.getElementById("crsInstructor").value = "";
    document.getElementById("crsFee").value = "";
    document.getElementById("crsSeats").value = "";
    document.getElementById("crsForm").classList.remove("hidden");
}

function hideCrsForm() {
    document.getElementById("crsForm").classList.add("hidden");
}

async function saveCourse() {
    if (!validateCourse()) return;
    const id = document.getElementById("crsId").value;
    const crs = {
        crsName: document.getElementById("crsName").value.trim(),
        duration: parseInt(document.getElementById("crsDuration").value),
        instructorName: document.getElementById("crsInstructor").value.trim(),
        crsFee: parseFloat(document.getElementById("crsFee").value) || 0,
        availableSeats: parseInt(document.getElementById("crsSeats").value)
    };
    const url = id ? `${API}/courses/${id}` : `${API}/courses`;
    const method = id ? "PUT" : "POST";
    const res = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(crs)
    });
    if (res.ok) {
        showToast(id ? "Course updated!" : "Course added!");
        hideCrsForm();
        loadCourses();
    } else {
        showToast("Something went wrong", "error");
    }
    loadStats();
}

async function editCourse(id) {
    const res = await fetch(`${API}/courses/${id}`);
    const c = await res.json();
    document.getElementById("crsFormTitle").textContent = "Edit Course";
    document.getElementById("crsId").value = c.id;
    document.getElementById("crsName").value = c.crsName;
    document.getElementById("crsDuration").value = c.duration;
    document.getElementById("crsInstructor").value = c.instructorName || "";
    document.getElementById("crsFee").value = c.crsFee;
    document.getElementById("crsSeats").value = c.availableSeats;
    document.getElementById("crsForm").classList.remove("hidden");
}

async function deleteCourse(id) {
    if (!confirm("Delete this course?")) return;
    const res = await fetch(`${API}/courses/${id}`, { method: "DELETE" });
    if (res.ok) { showToast("Course deleted!"); loadCourses(); }
    else showToast("Cannot delete", "error");
}


async function loadStudents() {
    const res = await fetch(`${API}/students`);
    const data = await res.json();
    renderStudents(data);
    loadStats();
}

function renderStudents(data) {
    const tbody = document.getElementById("stdBody");
    tbody.innerHTML = "";
    data.forEach(s => {
        const badge = s.status === "ACTIVE"
            ? `<span class="badge-active">Active</span>`
            : `<span class="badge-inactive">Inactive</span>`;
        tbody.innerHTML += `
        <tr>
            <td>${s.id}</td>
            <td>${s.stdName}</td>
            <td>${s.email}</td>
            <td>${s.phNumber || "-"}</td>
            <td>${s.crs ? s.crs.crsName : "-"}</td>
            <td>${s.enrollDate || "-"}</td>
            <td>${badge}</td>
            <td>
                <button class="btn-edit" onclick="editStudent(${s.id})">Edit</button>
                <button class="btn-delete" onclick="deleteStudent(${s.id})">Delete</button>
            </td>
        </tr>`;
    });
}

async function loadCoursesDropdown() {
    const res = await fetch(`${API}/courses`);
    const data = await res.json();
    const sel = document.getElementById("stdCourse");
    sel.innerHTML = `<option value="">-- Select Course --</option>`;
    data.forEach(c => {
        sel.innerHTML += `<option value="${c.id}">${c.crsName} (${c.availableSeats} seats)</option>`;
    });
}

function showStdForm() {
    document.getElementById("stdFormTitle").textContent = "Add Student";
    document.getElementById("stdId").value = "";
    document.getElementById("stdName").value = "";
    document.getElementById("stdEmail").value = "";
    document.getElementById("stdPhone").value = "";
    document.getElementById("stdStatus").value = "ACTIVE";
    document.getElementById("stdCourse").value = "";
    document.getElementById("stdForm").classList.remove("hidden");
}

function hideStdForm() {
    document.getElementById("stdForm").classList.add("hidden");
}

async function saveStudent() {
    if (!validateStudent()) return;
    const id = document.getElementById("stdId").value;
    const crsId = document.getElementById("stdCourse").value;
    const std = {
        stdName: document.getElementById("stdName").value.trim(),
        email: document.getElementById("stdEmail").value.trim(),
        phNumber: document.getElementById("stdPhone").value.trim(),
        status: document.getElementById("stdStatus").value,
        crs: crsId ? { id: parseInt(crsId) } : null
    };
    const url = id ? `${API}/students/${id}` : `${API}/students`;
    const method = id ? "PUT" : "POST";
    const res = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(std)
    });
    if (res.ok) {
        showToast(id ? "Student updated!" : "Student enrolled!");
        hideStdForm();
        loadStudents();
        loadCoursesDropdown();
    } else {
        const err = await res.text();
        showToast(err || "Something went wrong", "error");
    }
}

async function editStudent(id) {
    const res = await fetch(`${API}/students/${id}`);
    const s = await res.json();
    document.getElementById("stdFormTitle").textContent = "Edit Student";
    document.getElementById("stdId").value = s.id;
    document.getElementById("stdName").value = s.stdName;
    document.getElementById("stdEmail").value = s.email;
    document.getElementById("stdPhone").value = s.phNumber || "";
    document.getElementById("stdStatus").value = s.status;
    document.getElementById("stdCourse").value = s.crs ? s.crs.id : "";
    document.getElementById("stdForm").classList.remove("hidden");
}

async function deleteStudent(id) {
    if (!confirm("Delete this student?")) return;
    const res = await fetch(`${API}/students/${id}`, { method: "DELETE" });
    if (res.ok) { showToast("Student deleted!"); loadStudents(); loadCoursesDropdown(); }
    else showToast("Cannot delete", "error");
}

async function searchStudents() {
    const name = document.getElementById("searchName").value.trim();
    if (!name) { loadStudents(); return; }
    const res = await fetch(`${API}/students/search?name=${name}`);
    const data = await res.json();
    renderStudents(data);
}

async function filterStudents() {
    const crs = document.getElementById("filterCrs").value.trim();
    if (!crs) { loadStudents(); return; }
    const res = await fetch(`${API}/students/filter?crsName=${crs}`);
    const data = await res.json();
    renderStudents(data);
}


loadCourses();
loadStats();