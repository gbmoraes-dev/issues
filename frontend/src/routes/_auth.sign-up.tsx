import { useForm } from '@tanstack/react-form'
import { createFileRoute, Link, useNavigate } from '@tanstack/react-router'
import { Eye, EyeOff, Loader2 } from 'lucide-react'
import { useState } from 'react'
import { toast } from 'sonner'
import { z } from 'zod'
import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Field, FieldError, FieldLabel } from '@/components/ui/field'
import { Input } from '@/components/ui/input'
import { signUp } from '@/lib/auth'

export const Route = createFileRoute('/_auth/sign-up')({
  component: SignUpPage,
})

const signUpSchema = z.object({
  name: z.string().min(1, 'Name is required'),
  email: z.string().email('Enter a valid email'),
  password: z.string().min(8, 'Password must be at least 8 characters'),
})

function SignUpPage() {
  const navigate = useNavigate()
  const [showPassword, setShowPassword] = useState(false)

  const form = useForm({
    defaultValues: { name: '', email: '', password: '' },
    validators: { onChange: signUpSchema },
    onSubmit: async ({ value }) => {
      try {
        await signUp(value)
        navigate({ to: '/' })
      } catch {
        toast.error('Could not create account. Please try again.')
      }
    },
  })

  return (
    <Card className="w-full max-w-sm">
      <CardHeader>
        <CardTitle className="text-xl">Create an account</CardTitle>
        <CardDescription>Sign up to start tracking your issues</CardDescription>
      </CardHeader>
      <CardContent>
        <form
          onSubmit={(e) => {
            e.preventDefault()
            e.stopPropagation()
            form.handleSubmit()
          }}
          className="space-y-4"
        >
          <form.Field name="name">
            {(field) => {
              const error = field.state.meta.isTouched
                ? field.state.meta.errors[0]?.message
                : undefined
              return (
                <Field>
                  <FieldLabel htmlFor={field.name} error={!!error}>
                    Name
                  </FieldLabel>
                  <Input
                    id={field.name}
                    placeholder="Your name"
                    autoFocus
                    autoComplete="name"
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

          <form.Field name="email">
            {(field) => {
              const error = field.state.meta.isTouched
                ? field.state.meta.errors[0]?.message
                : undefined
              return (
                <Field>
                  <FieldLabel htmlFor={field.name} error={!!error}>
                    Email
                  </FieldLabel>
                  <Input
                    id={field.name}
                    type="email"
                    placeholder="you@example.com"
                    autoComplete="email"
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

          <form.Field name="password">
            {(field) => {
              const error = field.state.meta.isTouched
                ? field.state.meta.errors[0]?.message
                : undefined
              return (
                <Field>
                  <FieldLabel htmlFor={field.name} error={!!error}>
                    Password
                  </FieldLabel>
                  <div className="relative">
                    <Input
                      id={field.name}
                      type={showPassword ? 'text' : 'password'}
                      placeholder="Min. 8 characters"
                      autoComplete="new-password"
                      className="pr-9"
                      aria-invalid={!!error}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword((v) => !v)}
                      className="absolute right-2.5 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                      aria-label={
                        showPassword ? 'Hide password' : 'Show password'
                      }
                    >
                      {showPassword ? (
                        <EyeOff className="size-4" />
                      ) : (
                        <Eye className="size-4" />
                      )}
                    </button>
                  </div>
                  <FieldError>{error}</FieldError>
                </Field>
              )
            }}
          </form.Field>

          <form.Subscribe selector={(s) => [s.isSubmitting]}>
            {([isSubmitting]) => (
              <Button type="submit" className="w-full" disabled={isSubmitting}>
                {isSubmitting && <Loader2 className="animate-spin" />}
                Create account
              </Button>
            )}
          </form.Subscribe>
        </form>

        <p className="mt-4 text-center text-sm text-muted-foreground">
          Already have an account?{' '}
          <Link
            to="/sign-in"
            className="text-foreground underline underline-offset-4 hover:opacity-80"
          >
            Sign in
          </Link>
        </p>
      </CardContent>
    </Card>
  )
}
