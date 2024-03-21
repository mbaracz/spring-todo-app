<script lang="ts">
    import { removeTodoList, store, updateTodoListId } from "../lib/store";
    import { createEventDispatcher, onMount } from "svelte";
    import { createTodoList, updateTodoList } from "../lib/requests";
    import type { Todolist } from "../lib/todolist";
    import TodoTask from "./TodoTask.svelte";
    import { fade } from "svelte/transition";
    import type { Unsubscriber } from "svelte/store";

    let taskInput: string;
    let subscriber: Unsubscriber | null = null;
    const dispatch = createEventDispatcher();

    function clearTasks() {
        $store.tasks = [];
    }

    function deleteList() {
        subscriber!();
        removeTodoList();
        dispatch("todoListDelete");
    }

    async function addTask(event: KeyboardEvent) {
        if (event.key !== "Enter" || !taskInput?.trim().length) {
            return;
        }

        const task = { description: taskInput, completed: false };

        if (!$store.id) {
            const id = await createTodoList(task);
            const todolist = { id, tasks: [task] } as Todolist;

            // Update todolist id stored in localStorage
            updateTodoListId(id);

            // Update writable with todolist
            store.set(todolist);

            // Register store subscriber after we get todolist id
            registerStoreSubscriber();
        } else {
            $store.tasks.push(task);
            store.update(todolist => todolist);
        }

        (event.target as HTMLInputElement).value = "";
    }

    function registerStoreSubscriber() {
        if (subscriber) return;

        let fnSubscriber = store.subscribe(data => {
            if (!subscriber) {
                return;
            }
            console.log(data)
            updateTodoList(data);
        });

        subscriber = fnSubscriber;
    }

    // Register store subscriber if store already exists
    onMount(() => {
        if ($store.id) {
            registerStoreSubscriber();
        }
    });
</script>

<div class="todolist" transition:fade>
    <div class="header">
        {#if $store.tasks?.length}
            <button on:click={clearTasks}>Clear tasks</button>
        {:else}
            <div></div>
        {/if}

        <h2>Todo List</h2>

        {#if $store.id}
            <button on:click={deleteList}>Delete list</button>
        {:else}
            <div></div>
        {/if}
    </div>

    <input type="text"
           class="task-input"
           placeholder="What needs to be done?"
           on:keydown={addTask}
           bind:value={taskInput}
           autofocus>

    <div class="tasks">
        {#if $store.tasks }
            {#each $store.tasks as task}
                <TodoTask {task}/>
            {/each}
        {/if}
    </div>
</div>

<style>
    .todolist {
        min-height: 60vh;
        padding: 5px;
        background-color: #2d2d2d;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        border-radius: 1rem;
    }

    .todolist .task-input {
        background: #242424;
        border: none;
        width: 85%;
        outline: none;
        border-radius: 15px;
        padding: 10px 15px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        margin-bottom: 10px;
        text-align: center;
    }

    .todolist .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 15px;
    }

    .todolist .header h2 {
        flex: 50%;
    }

    .todolist button {
        background-color: #ff4d4d;
        color: white;
        border: none;
        padding: 8px 16px;
        border-radius: 5px;
        cursor: pointer;
    }

    .todolist .header > * {
        flex: 25%;
    }
</style>
