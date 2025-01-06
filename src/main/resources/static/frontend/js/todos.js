// BASE_URL 정의
const BASE_URL = window.location.origin.includes('localhost')
    ? 'http://localhost:8080'
    : 'https://todo-project-j3jq.onrender.com';

// 인증 관련 경로
const AUTH_URL = `${BASE_URL}/auth`;
// Todo 관련 경로
const TODO_URL = `${BASE_URL}/api/todos`;

// Axios 요청 인터셉터 설정
axios.interceptors.request.use(config => {
    const token = localStorage.getItem('jwt_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    } else {
        console.warn("JWT 토큰이 없습니다.");
    }
    return config;
}, error => Promise.reject(error));

// Axios 응답 인터셉터 설정
axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('jwt_token');
            alert('로그인이 필요합니다.');
            window.location.href = '/frontend/pages/login.html';
        } else {
            console.error('Axios 응답 오류:', error.response || error.message);
        }
        return Promise.reject(error);
    }
);

// Todo 목록 불러오기
async function fetchTodos() {
    try {
        const response = await axios.get(TODO_URL);
        updateUI(response.data);
    } catch (error) {
        console.error('Todo 목록을 불러오는 중 오류 발생:', error);
    }
}

// 화면에 Todo 항목 업데이트
function updateUI(todos) {
    const list = document.getElementById('todo-list');
    list.innerHTML = '';
    if (!todos || todos.length === 0) {
        const emptyMessage = document.createElement('p');
        emptyMessage.textContent = '등록된 Todo가 없습니다.';
        emptyMessage.classList.add('text-center', 'text-muted');
        list.appendChild(emptyMessage);
        return;
    }

    todos.forEach(todo => {
        const li = document.createElement('li');
        li.className = `list-group-item d-flex justify-content-between align-items-center ${todo.isCompleted ? 'completed' : ''}`;
        li.innerHTML = `
            <span onclick="toggleTodo(${todo.id})" style="cursor: pointer;">
                ${todo.task}
            </span>
            <div>
                <button class="btn btn-warning btn-sm" onclick="editTodoPrompt(${todo.id})">수정</button>
                <button class="btn btn-danger btn-sm" onclick="deleteTodoWithAnimation(${todo.id}, this)">삭제</button>
            </div>
        `;
        list.appendChild(li);
    });
}

// Todo 추가
document.getElementById('todo-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const taskInput = document.getElementById('task-input');
    const newTask = taskInput.value.trim();

    if (!newTask) {
        alert('할 일을 입력하세요.');
        return;
    }

    try {
        await axios.post(TODO_URL, { task: newTask, isCompleted: false });
        taskInput.value = '';
        fetchTodos();
    } catch (error) {
        console.error('Todo 추가 중 오류 발생:', error);
    }
});

// Todo 완료 상태 토글
async function toggleTodo(id) {
    try {
        const response = await axios.patch(`${TODO_URL}/${id}/toggle`);
        console.log(`Todo 상태 변경됨: ${response.data}`);
        fetchTodos();
    } catch (error) {
        console.error('Todo 완료 상태 변경 중 오류 발생:', error);
    }
}

// Todo 수정
async function editTodoPrompt(id) {
    const newTask = prompt('수정할 내용을 입력하세요:').trim();
    if (!newTask) {
        alert('수정할 내용을 입력하세요.');
        return;
    }

    try {
        await axios.put(`${TODO_URL}/${id}`, { task: newTask, isCompleted: false });
        fetchTodos();
    } catch (error) {
        console.error('Todo 수정 중 오류 발생:', error);
    }
}

// Todo 삭제
async function deleteTodoWithAnimation(id, button) {
    try {
        const item = button.closest('li');
        if (item) {
            item.style.transition = 'opacity 0.3s';
            item.style.opacity = '0';
            setTimeout(() => item.remove(), 300);
        }

        await axios.delete(`${TODO_URL}/${id}`);
        console.log(`Todo 삭제 완료: ${id}`);
        fetchTodos();
    } catch (error) {
        console.error('Todo 삭제 중 오류 발생:', error);
    }
}

// 완료된 Todo 삭제
async function deleteCompletedTodos() {
    try {
        const confirmation = confirm('완료된 Todo를 모두 삭제하시겠습니까?');
        if (!confirmation) return;

        await axios.delete(`${TODO_URL}/completed`);
        console.log('완료된 Todo가 삭제되었습니다.');
        fetchTodos();
    } catch (error) {
        console.error('완료된 Todo 삭제 중 오류 발생:', error);
    }
}

// Todo 필터링
async function filterTodos(isCompleted) {
    try {
        const response = await axios.get(`${TODO_URL}/filter`, {
            params: { isCompleted },
        });
        updateUI(response.data);
    } catch (error) {
        console.error('Todo 필터링 중 오류 발생:', error);
    }
}

// 필터 버튼 이벤트
document.getElementById('filter-all').addEventListener('click', fetchTodos);
document.getElementById('filter-completed').addEventListener('click', () => filterTodos(true));
document.getElementById('filter-incomplete').addEventListener('click', () => filterTodos(false));

document.getElementById('delete-completed').addEventListener('click', deleteCompletedTodos);

// 페이지 로드 시 Todo 목록 불러오기
document.addEventListener('DOMContentLoaded', fetchTodos);

// 로그아웃 버튼 동작
document.getElementById('logout-button').addEventListener('click', () => {
    const confirmation = confirm('로그아웃 하시겠습니까?');
    if (confirmation) {
        // JWT 토큰 삭제
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('jwt_expiresAt'); // 만료 시간도 삭제 (필요 시)

        // 로그인 페이지로 리다이렉트
        window.location.href = '/frontend/pages/login.html';
    }
});
