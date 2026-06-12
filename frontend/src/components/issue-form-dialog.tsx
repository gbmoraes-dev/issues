import { useForm } from '@tanstack/react-form'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { Loader2 } from 'lucide-react'
import { useEffect } from 'react'
import { toast } from 'sonner'
import { z } from 'zod'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Field, FieldError, FieldLabel } from '@/components/ui/field'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import {
  createIssue,
  type Issue,
  issuesQueryKey,
  updateIssue,
} from '@/lib/issues'

const issueSchema = z.object({
  title: z
    .string()
    .min(1, 'Title is required')
    .max(100, 'Title must be at most 100 characters'),
  description: z.string(),
})

interface IssueFormDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  issue?: Issue
}

export function IssueFormDialog({
  open,
  onOpenChange,
  issue,
}: IssueFormDialogProps) {
  const isEditing = issue !== undefined
  const queryClient = useQueryClient()

  const { mutateAsync } = useMutation({
    mutationFn: (values: { title: string; description: string }) =>
      isEditing ? updateIssue(issue.id, values) : createIssue(values),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: issuesQueryKey })
      toast.success(isEditing ? 'Issue updated' : 'Issue created')
    },
  })

  const form = useForm({
    defaultValues: {
      title: issue?.title ?? '',
      description: issue?.description ?? '',
    },
    validators: { onChange: issueSchema },
    onSubmit: async ({ value }) => {
      await mutateAsync(value)
      form.reset()
      onOpenChange(false)
    },
  })

  useEffect(() => {
    if (open) {
      form.reset({
        title: issue?.title ?? '',
        description: issue?.description ?? '',
      })
    }
  }, [open, issue, form.reset])

  function handleOpenChange(next: boolean) {
    if (!next) form.reset()
    onOpenChange(next)
  }

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogContent className="sm:max-w-lg">
        <DialogHeader>
          <DialogTitle>{isEditing ? 'Edit issue' : 'Create issue'}</DialogTitle>
        </DialogHeader>

        <form
          onSubmit={(e) => {
            e.preventDefault()
            e.stopPropagation()
            form.handleSubmit()
          }}
          className="space-y-4"
        >
          <form.Field name="title">
            {(field) => {
              const error = field.state.meta.isTouched
                ? field.state.meta.errors[0]?.message
                : undefined
              return (
                <Field>
                  <FieldLabel htmlFor={field.name} error={!!error}>
                    Title
                  </FieldLabel>
                  <Input
                    id={field.name}
                    placeholder="Issue title"
                    autoFocus
                    aria-invalid={!!error}
                    value={field.state.value}
                    onBlur={field.handleBlur}
                    onChange={(e) => field.handleChange(e.target.value)}
                  />
                  <FieldError>{error}</FieldError>
                </Field>
              )
            }}
          </form.Field>

          <form.Field name="description">
            {(field) => {
              const error = field.state.meta.isTouched
                ? field.state.meta.errors[0]?.message
                : undefined
              return (
                <Field>
                  <FieldLabel htmlFor={field.name} error={!!error}>
                    Description
                  </FieldLabel>
                  <Textarea
                    id={field.name}
                    placeholder="Add description…"
                    rows={5}
                    className="resize-none"
                    aria-invalid={!!error}
                    value={field.state.value}
                    onBlur={field.handleBlur}
                    onChange={(e) => field.handleChange(e.target.value)}
                  />
                  <FieldError>{error}</FieldError>
                </Field>
              )
            }}
          </form.Field>

          <div className="flex justify-end gap-2 pt-2">
            <DialogClose asChild>
              <Button type="button" variant="ghost">
                Cancel
              </Button>
            </DialogClose>
            <form.Subscribe selector={(s) => [s.isSubmitting]}>
              {([isSubmitting]) => (
                <Button type="submit" disabled={isSubmitting}>
                  {isSubmitting && <Loader2 className="animate-spin" />}
                  {isEditing ? 'Save changes' : 'Create issue'}
                </Button>
              )}
            </form.Subscribe>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  )
}
