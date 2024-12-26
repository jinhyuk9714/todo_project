const API_URL = 'https://todo-project-j3jq.onrender.com/api/todos';

// Todo 목록 불러오기
async function fetchTodos() {
    try {
        const response = await axios.get(API_URL);
        updateUI(response.data); // 데이터를 화면에 업데이트
    } catch (error) {
        console.error('Todo 목록을 불러오는 중 오류 발생:', error);
        updateUI([]); // 오류 시 빈 목록 표시
    }
}

// 화면에 Todo 항목 업데이트
function updateUI(todos) {
    const list = document.getElementById('todo-list');
    list.innerHTML = '';
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
        addAnimation(li, 'added');
    });
}

// Todo 수정 요청
async function editTodoPrompt(id) {
    const newTask = prompt("수정할 내용을 입력하세요:");
    if (!newTask) {
        alert("내용이 비어 있습니다.");
        return;
    }

    try {
        await axios.put(`${API_URL}/${id}`, { task: newTask, isCompleted: false });
        fetchTodos(); // 수정 후 목록 갱신
    } catch (error) {
        console.error('Todo 수정 중 오류 발생:', error);
    }
}

// Todo 완료 상태 토글
async function toggleTodo(id) {
    try {
        await axios.patch(`${API_URL}/${id}/toggle`);
        fetchTodos(); // 목록 갱신
    } catch (error) {
        console.error('Todo 완료 상태 변경 중 오류 발생:', error);
    }
}

// Todo 추가
document.getElementById('todo-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const taskInput = document.getElementById('task-input');
    const newTask = taskInput.value;

    try {
        await axios.post(API_URL, { task: newTask, isCompleted: false });
        taskInput.value = '';
        fetchTodos(); // 추가 후 목록 갱신
    } catch (error) {
        console.error('Todo 추가 중 오류 발생:', error);
    }
});

// Todo 삭제
async function deleteTodoWithAnimation(id, button) {
    try {
        const item = button.closest('li');
        addAnimation(item, 'removed', async () => {
            await axios.delete(`${API_URL}/${id}`);
            fetchTodos(); // 삭제 후 목록 갱신
        });
    } catch (error) {
        console.error('Todo 삭제 중 오류 발생:', error);
    }
}

// 완료된 Todo 삭제
async function deleteCompletedTodos() {
    try {
        await axios.delete(`${API_URL}/completed`);
        fetchTodos(); // 삭제 후 목록 갱신
    } catch (error) {
        console.error('완료된 Todo 삭제 중 오류 발생:', error);
    }
}

// Todo 필터링
async function filterTodos(filter) {
    try {
        const response = await axios.get(`${API_URL}/filter`, {
            params: { isCompleted: filter }
        });
        updateUI(response.data); // 필터링 결과 화면에 반영
    } catch (error) {
        console.error('Todo 필터링 중 오류 발생:', error);
    }
}

// 애니메이션 추가
function addAnimation(item, className, callback) {
    item.classList.add(className);
    setTimeout(() => {
        item.classList.remove(className);
        if (callback) callback();
    }, 300);
}

// 필터 버튼 이벤트
document.getElementById('filter-completed').addEventListener('click', () => {
    filterTodos(true);
});
document.getElementById('filter-incomplete').addEventListener('click', () => {
    filterTodos(false);
});
document.getElementById('filter-all').addEventListener('click', () => {
    fetchTodos();
});

// 완료된 Todo 삭제 버튼 이벤트
document.getElementById('delete-completed').addEventListener('click', async () => {
    await deleteCompletedTodos();
});

// 페이지 로드 시 Todo 목록 불러오기
document.addEventListener('DOMContentLoaded', () => {
    fetchTodos();
});
