<script lang="ts">
    import { onMount } from "svelte";
    import TodoList from "./components/TodoList.svelte";
    import { getTodoListId, store } from "./lib/store";
    import type { Todolist } from "./lib/todolist";
    import { fade } from "svelte/transition";

    let clicked = false;

    let loading = true;

    async function fetchTodoList(id: string) {
        fetch(import.meta.env.VITE_ENDPOINT_URL + id)
            .then(response => {
                if (response.status === 404) {
                    localStorage.removeItem("todoListId");
                    return null;
                }
                return response.json();
            })
            .then((todolist?: Todolist) => {
                if (todolist) {
                    store.set(todolist);
                }
            })
            .finally(() => loading = false);
    }

    function createTodoList() {
        clicked = true;
    }

    function handleTodoListDelete() {
        clicked = false;
    }

    onMount(async () => {
        const todoListId = getTodoListId();

        if (todoListId) {
            await fetchTodoList(todoListId);
        } else {
            loading = false;
        }
    });
</script>

<main>
    <h1>Welcome to Todo App</h1>

    {#if loading}
        <p>Loading...</p>
    {:else}
        {#if getTodoListId() || clicked}
            <TodoList on:todoListDelete={handleTodoListDelete}/>
        {:else}
            <div in:fade>
                <button on:click={createTodoList}>Create new todo list</button>
            </div>
        {/if}
    {/if}
</main>