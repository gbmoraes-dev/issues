import { createFileRoute, Outlet, redirect } from '@tanstack/react-router'
import { Header } from '@/components/header'
import { isAuthenticated } from '@/lib/auth'

export const Route = createFileRoute('/_app')({
  beforeLoad() {
    if (!isAuthenticated()) {
      throw redirect({ to: '/sign-in' })
    }
  },
  component: AppLayout,
})

function AppLayout() {
  return (
    <>
      <Header />
      <main>
        <Outlet />
      </main>
    </>
  )
}
