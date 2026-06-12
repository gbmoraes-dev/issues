import { useInfiniteQuery } from '@tanstack/react-query'
import { Loader2 } from 'lucide-react'
import { useMemo } from 'react'
import { IssueRow } from '@/components/issue-row'
import { Button } from '@/components/ui/button'
import { issuesQueryKey, listIssuesPage, sortIssues } from '@/lib/issues'

export function IssueList() {
  const { data, isLoading, hasNextPage, fetchNextPage, isFetchingNextPage } =
    useInfiniteQuery({
      queryKey: issuesQueryKey,
      queryFn: ({ pageParam }) => listIssuesPage(pageParam),
      getNextPageParam: (lastPage) => lastPage.nextCursor ?? undefined,
      initialPageParam: undefined as string | undefined,
    })

  const issues = useMemo(
    () => sortIssues(data?.pages.flatMap((p) => p.items) ?? []),
    [data],
  )

  if (isLoading) {
    return (
      <div className="mx-auto max-w-3xl rounded-md overflow-hidden divide-y divide-border">
        {['sk-1', 'sk-2', 'sk-3'].map((key) => (
          <div key={key} className="flex items-center gap-3 px-4 h-11">
            <div className="size-4 rounded-sm bg-muted animate-pulse shrink-0" />
            <div className="h-3 rounded bg-muted animate-pulse w-48" />
          </div>
        ))}
      </div>
    )
  }

  if (!issues.length) {
    return (
      <div className="mx-auto max-w-3xl flex flex-col items-center justify-center py-24 text-muted-foreground">
        <p className="text-sm">No issues yet. Create one to get started.</p>
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-3xl">
      <div className="rounded-md overflow-hidden divide-y divide-border">
        {issues.map((issue) => (
          <IssueRow key={issue.id} issue={issue} />
        ))}
      </div>

      {hasNextPage && (
        <div className="flex justify-center mt-4">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => fetchNextPage()}
            disabled={isFetchingNextPage}
          >
            {isFetchingNextPage && <Loader2 className="animate-spin" />}
            Load more
          </Button>
        </div>
      )}
    </div>
  )
}
