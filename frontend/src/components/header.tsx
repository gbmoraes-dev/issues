import { Link, useNavigate } from '@tanstack/react-router'
import { LogOut } from 'lucide-react'
import { CreateIssueDialog } from '@/components/create-issue-dialog'
import { Button } from '@/components/ui/button'
import { signOut } from '@/lib/auth'

export function Header() {
  const navigate = useNavigate()

  function handleSignOut() {
    signOut()
    navigate({ to: '/sign-in' })
  }

  return (
    <header className="sticky top-0 z-50 bg-background/95 backdrop-blur supports-backdrop:bg-background/60 after:absolute after:inset-x-0 after:bottom-0 after:h-px after:bg-linear-to-r after:from-transparent after:via-border/70 after:to-transparent">
      <div className="flex h-18 items-center justify-between px-4 sm:px-6">
        <Link
          to="/"
          className="text-base font-semibold tracking-tight hover:opacity-80 transition-opacity"
        >
          Trackr
        </Link>

        <div className="flex items-center gap-2">
          <CreateIssueDialog />
          <Button
            variant="ghost"
            size="icon-sm"
            aria-label="Sign out"
            onClick={handleSignOut}
            className="opacity-40 hover:opacity-100 hover:text-destructive/90 hover:bg-destructive/10"
          >
            <LogOut />
          </Button>
        </div>
      </div>
    </header>
  )
}
