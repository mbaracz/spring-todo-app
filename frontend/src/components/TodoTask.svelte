<script lang="ts">
    import type { Task } from "../lib/task";
    import { fade } from "svelte/transition";
    import { store } from "../lib/store";
    import deleteIcon from "../assets/deleteIcon.svg";

    export let task: Task;

    function debounce<T extends Function>(func: T, delay: number): (...args: any[]) => void {
        let timer: number;

        return function(this: any, ...args: any[]) {
            clearTimeout(timer);
            timer = setTimeout(() => func.apply(this, args), delay);
        };
    }

    function deleteTask() {
        const tasks = $store.tasks;
        tasks.splice(tasks.indexOf(task), 1);
        store.update(todolist => todolist);
    }

    function updateDescription(event: Event) {
        task.description = (event.target as HTMLInputElement).value;
        store.update(todolist => todolist);
    }

    function updateCompletion(event: Event) {
        task.completed = (event.target as HTMLInputElement).checked;
        store.update(todolist => todolist);
    }

    const debouncedUpdateDescription = debounce(updateDescription, 300);
</script>

<div class="task" transition:fade>
    <input type="checkbox" bind:checked={task.completed} on:change={updateCompletion}>
    <input type="text" bind:value={task.description} on:input={debouncedUpdateDescription}>
    <img src={deleteIcon} on:click={deleteTask} alt="Delete task">
</div>

<style>
    .task {
        display: flex;
        align-items: center;
        height: 40px;
        border-radius: 5px;
        margin: 5px;
        background: #3d3d3d;
        padding: 0 0 0 5px;
    }

    .task input[type="checkbox"] {
        margin-right: 10px;
        border-radius: 50px;
        border: 1px solid white;
    }

    .task input[type="text"] {
        background: unset;
        border: none;
        width: 85%;
    }

    .task input[type="text"]:focus {
        border-bottom: 1px solid white;
        outline: none;
    }

    img {
        cursor: pointer;
    }
</style>