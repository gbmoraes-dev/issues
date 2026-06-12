import { useMutation, useQueryClient } from '@tanstack/react-query'
import { MoreHorizontal, Pencil, Trash2 } from 'lucide-react'
import { useState } from 'react'
import { toast } from 'sonner'
import { IssueDetailDialog } from '@/components/issue-detail-dialog'
import { IssueFormDialog } from '@/components/issue-form-dialog'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog'
import { Button } from '@/components/ui/button'
import { Checkbox } from '@/components/ui/checkbox'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import {
  deleteIssue,
  type Issue,
  issuesQueryKey,
  toggleIssue,
} from '@/lib/issues'
import { cn } from '@/lib/utils'

interface IssueRowProps {
  issue: Issue
}

export function IssueRow({ issue }: IssueRowProps) {
  const queryClient = useQueryClient()
  const [detailOpen, setDetailOpen] = useState(false)
  const [editOpen, setEditOpen] = useState(false)
  const [deleteOpen, setDeleteOpen] = useState(false)

  const { mutate: toggle, isPending: isToggling } = useMutation({
    mutationFn: () => toggleIssue(issue.id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: issuesQueryKey })
    },
  })

  const { mutate: remove, isPending: isDeleting } = useMutation({
    mutationFn: () => deleteIssue(issue.id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: issuesQueryKey })
      toast.success('Issue deleted')
    },
  })

  return (
    <>
      <div className="px-1 py-0.5">
        <div className="group flex items-center gap-3 px-3 h-10 rounded-md hover:bg-accent/40 transition-colors cursor-default select-none">
          <Checkbox
            checked={issue.completed}
            disabled={isToggling || isDeleting}
            onCheckedChange={() => toggle()}
            aria-label={issue.completed ? 'Mark as open' : 'Mark as done'}
            className="shrink-0"
          />

          <button
            type="button"
            className="flex flex-1 items-center gap-3 min-w-0 cursor-pointer text-left bg-transparent border-0 p-0"
            onClick={() => setDetailOpen(true)}
          >
            <span
              className={cn(
                'flex-1 text-sm truncate',
                issue.completed && 'line-through text-muted-foreground',
              )}
            >
              {issue.title}
            </span>

            {issue.description && (
              <span className="hidden sm:block text-xs text-muted-foreground truncate max-w-65">
                {issue.description}
              </span>
            )}
          </button>

          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button
                variant="ghost"
                size="icon-sm"
                aria-label="Issue actions"
                className="opacity-0 group-hover:opacity-100 focus-visible:opacity-100 data-[state=open]:opacity-100 shrink-0"
              >
                <MoreHorizontal />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuItem
                onSelect={(e) => {
                  e.preventDefault()
                  setEditOpen(true)
                }}
              >
                <Pencil />
                Edit
              </DropdownMenuItem>
              <DropdownMenuSeparator />
              <DropdownMenuItem
                variant="destructive"
                onSelect={(e) => {
                  e.preventDefault()
                  setDeleteOpen(true)
                }}
              >
                <Trash2 />
                Delete
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>

      <IssueDetailDialog
        issue={issue}
        open={detailOpen}
        onOpenChange={setDetailOpen}
      />
      <IssueFormDialog
        issue={issue}
        open={editOpen}
        onOpenChange={setEditOpen}
      />

      <AlertDialog open={deleteOpen} onOpenChange={setDeleteOpen}>
        <AlertDialogContent size="sm">
          <AlertDialogHeader>
            <AlertDialogTitle>Delete issue?</AlertDialogTitle>
            <AlertDialogDescription>
              This will permanently delete &quot;{issue.title}&quot;. This
              action cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction
              variant="destructive"
              disabled={isDeleting}
              onClick={() => remove()}
            >
              Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  )
}
