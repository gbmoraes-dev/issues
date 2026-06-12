import { createFileRoute } from '@tanstack/react-router'
import { IssueList } from '@/components/issue-list'

export const Route = createFileRoute('/_app/')({ component: Home })

function Home() {
  return (
    <div className="px-4 py-8">
      <IssueList />
    </div>
  )
}
