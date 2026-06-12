import { useSyncExternalStore } from 'react'

const TOKEN_KEY = 'trackr.auth.token'
const API_URL = import.meta.env.VITE_API_URL

type Listener = () => void
const listeners = new Set<Listener>()

function emit() {
  for (const l of listeners) l()
}

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function isAuthenticated(): boolean {
  return getToken() !== null
}

export function subscribe(listener: Listener): () => void {
  listeners.add(listener)
  return () => listeners.delete(listener)
}

function saveToken(token: string) {
  localStorage.setItem(TOKEN_KEY, token)
  emit()
}

export function signOut() {
  localStorage.removeItem(TOKEN_KEY)
  emit()
}

export async function signIn(input: {
  email: string
  password: string
}): Promise<void> {
  const res = await fetch(`${API_URL}/auth/sign-in`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  })

  if (!res.ok) throw new Error(`Sign in failed: ${res.status}`)

  const { token } = (await res.json()) as { token: string }

  saveToken(token)
}

export async function signUp(input: {
  name: string
  email: string
  password: string
}): Promise<void> {
  const res = await fetch(`${API_URL}/auth/sign-up`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  })

  if (!res.ok) throw new Error(`Sign up failed: ${res.status}`)

  const { token } = (await res.json()) as { token: string }

  saveToken(token)
}

export function useAuth(): string | null {
  return useSyncExternalStore(subscribe, getToken, () => null)
}
