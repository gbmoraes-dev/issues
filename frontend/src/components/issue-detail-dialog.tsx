import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import type { Issue } from '@/lib/issues'
import { cn } from '@/lib/utils'

interface IssueDetailDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  issue: Issue
}

export function IssueDetailDialog({
  open,
  onOpenChange,
  issue,
}: IssueDetailDialogProps) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-lg">
        <DialogHeader>
          <DialogTitle
            className={cn(
              'wrap-break-word leading-snug',
              issue.completed && 'line-through text-muted-foreground',
            )}
          >
            {issue.title}
          </DialogTitle>
        </DialogHeader>

        <div className="max-h-[60vh] overflow-y-auto">
          {issue.description ? (
            <p className="whitespace-pre-wrap wrap-break-word text-sm text-muted-foreground">
              {issue.description}
            </p>
          ) : (
            <p className="text-sm text-muted-foreground italic">
              No description provided.
            </p>
          )}
        </div>
      </DialogContent>
    </Dialog>
  )
}
