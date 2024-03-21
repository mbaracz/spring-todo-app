import type { Task } from "./task";

export type Todolist = {
    id: string,
    tasks: Array<Task>
    updatedAt: Date,
    createdAt: Date,
}