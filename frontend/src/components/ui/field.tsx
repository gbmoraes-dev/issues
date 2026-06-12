import type * as React from 'react'
import { Label } from '@/components/ui/label'
import { cn } from '@/lib/utils'

/** Wrapper que agrupa label + input + mensagem de erro. */
function Field({ className, ...props }: React.ComponentProps<'div'>) {
  return <div className={cn('grid gap-2', className)} {...props} />
}

/** Label que fica vermelho quando há erro. */
function FieldLabel({
  error,
  className,
  ...props
}: React.ComponentProps<typeof Label> & { error?: boolean }) {
  return (
    <Label
      data-error={error || undefined}
      className={cn('data-[error]:text-destructive', className)}
      {...props}
    />
  )
}

/** Mensagem de erro; não renderiza nada quando vazia. */
function FieldError({
  children,
  className,
  ...props
}: React.ComponentProps<'p'>) {
  if (!children) return null
  return (
    <p className={cn('text-sm text-destructive', className)} {...props}>
      {children}
    </p>
  )
}

export { Field, FieldError, FieldLabel }
