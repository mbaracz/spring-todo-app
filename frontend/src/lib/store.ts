import { writable } from "svelte/store";
import type { Todolist } from "./todolist";

export const store = writable<Todolist>({} as Todolist);

const TODO_LIST_ID_KEY = "todoListId";

export function getTodoListId(): string | null {
    return localStorage.getItem(TODO_LIST_ID_KEY);
}

export function updateTodoListId(id: string) {
    localStorage.setItem(TODO_LIST_ID_KEY, id);
}

export function removeTodoList() {
    localStorage.clear();
    store.set({} as Todolist);
}