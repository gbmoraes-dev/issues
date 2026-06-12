import { _delete, complete, create, list, update } from '@/api/endpoints'
import type { IssueResponse } from '@/api/model'

export interface Issue {
  id: string
  title: string
  description: string
  completed: boolean
  createdAt: string
}

export interface IssuePage {
  items: Issue[]
  nextCursor: string | null
  hasMore: boolean
}

function toIssue(r: IssueResponse): Issue {
  return {
    id: r.id!,
    title: r.title!,
    description: r.description ?? '',
    completed: r.completed ?? false,
    createdAt: r.createdAt!,
  }
}

export const issuesQueryKey = ['issues'] as const

export async function listIssuesPage(cursor?: string): Promise<IssuePage> {
  const response = await list({ limit: 20, cursor })
  return {
    items: (response.items ?? []).map(toIssue),
    nextCursor: response.nextCursor ?? null,
    hasMore: response.hasMore ?? false,
  }
}

export function sortIssues(issues: Issue[]): Issue[] {
  return [...issues].sort((a, b) => {
    if (a.completed !== b.completed) return a.completed ? 1 : -1
    return b.createdAt.localeCompare(a.createdAt)
  })
}

export async function createIssue(input: {
  title: string
  description: string
}): Promise<Issue> {
  const result = await create({
    title: input.title,
    description: input.description,
  })
  return toIssue(result)
}

export async function toggleIssue(id: string): Promise<Issue> {
  const result = await complete(id)
  return toIssue(result)
}

export async function updateIssue(
  id: string,
  input: { title: string; description: string },
): Promise<Issue> {
  const result = await update(id, {
    title: input.title,
    description: input.description,
  })
  return toIssue(result)
}

export async function deleteIssue(id: string): Promise<void> {
  await _delete(id)
}
