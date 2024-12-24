const API_URL = 'http://localhost:8080/api/todos';

// Fetch all todos from the API
async function fetchTodos() {
    try {
        const response = await axios.get(API_URL);
        updateUI(response.data); // 데이터를 UI에 업데이트
    } catch (error) {
        console.error('Error fetching todos:', error);
        updateUI([]); // 오류가 발생한 경우 UI를 빈 상태로 설정
    }
}

// Update the UI with the given todos
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
            <button class="btn btn-danger btn-sm" onclick="deleteTodoWithAnimation(${todo.id}, this)">Delete</button>
        `;
        list.appendChild(li);

        // Add animation for newly added items
        addAnimation(li, 'added');
    });
}

// Toggle the completion status of a todo
async function toggleTodo(id) {
    try {
        await axios.patch(`${API_URL}/${id}/toggle`);
        fetchTodos(); // 목록 갱신
    } catch (error) {
        console.error('Error toggling todo:', error);
    }
}

// Add a new todo
document.getElementById('todo-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const taskInput = document.getElementById('task-input');
    const newTask = taskInput.value;

    try {
        await axios.post(API_URL, { task: newTask, isCompleted: false });
        taskInput.value = ''; // 입력 필드 초기화
        fetchTodos(); // 목록 갱신
    } catch (error) {
        console.error('Error adding todo:', error);
    }
});

// Delete a todo with animation
async function deleteTodoWithAnimation(id, button) {
    try {
        const item = button.closest('li');
        addAnimation(item, 'removed', async () => {
            await axios.delete(`${API_URL}/${id}`);
            fetchTodos(); // 목록 갱신
        });
    } catch (error) {
        console.error('Error deleting todo:', error);
    }
}

// Delete all completed todos using the backend API
async function deleteCompletedTodos() {
    try {
        await axios.delete(`${API_URL}/completed`); // Backend API for deleting completed todos
        fetchTodos(); // 목록 갱신
    } catch (error) {
        console.error('Error deleting completed todos:', error);
    }
}

// Filter todos using the backend API
async function filterTodos(filter) {
    try {
        const response = await axios.get(`${API_URL}/filter`, {
            params: { isCompleted: filter } // 문자열 값 전달
        });
        updateUI(response.data); // 필터링된 데이터로 UI 갱신
    } catch (error) {
        console.error('Error filtering todos:', error);
    }
}

// Add animation to list items
function addAnimation(item, className, callback) {
    item.classList.add(className);
    setTimeout(() => {
        item.classList.remove(className);
        if (callback) callback();
    }, 300); // Animation duration
}

// Event listeners for filter buttons
document.getElementById('filter-completed').addEventListener('click', () => {
    filterTodos(true); // 완료된 항목 필터링
});
document.getElementById('filter-incomplete').addEventListener('click', () => {
    filterTodos(false); // 미완료 항목 필터링
});
document.getElementById('filter-all').addEventListener('click', () => {
    fetchTodos(); // 모든 항목 보기
});

document.getElementById('delete-completed').addEventListener('click', async () => {
    const response = await axios.delete(`${API_URL}/completed`); // DELETE 요청 보내기
    fetchTodos(); // 목록 갱신
});


// Initial fetch of todos on page load
document.addEventListener('DOMContentLoaded', () => {
    fetchTodos(); // 초기 로드시 모든 할 일 항목 로드
});
