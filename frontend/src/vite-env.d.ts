/// <reference types="svelte" />
/// <reference types="vite/client" />

interface ImportMetaEnv {
    readonly VITE_ENDPOINT_URL: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}