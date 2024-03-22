import type { Task } from "./task";
import type { Todolist } from "./todolist";

export async function createTodoList(initialTask: Task): Promise<string> {
    const payload = { "tasks": [initialTask] };

    return fetch(import.meta.env.VITE_ENDPOINT_URL, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    }).then(response => {
        return response.json();
    });
}

export async function updateTodoList(todolist: Todolist): Promise<Response> {
    return fetch(import.meta.env.VITE_ENDPOINT_URL, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(todolist)
    });
}