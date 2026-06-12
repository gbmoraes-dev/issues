import { Plus } from 'lucide-react'
import { useState } from 'react'
import { IssueFormDialog } from '@/components/issue-form-dialog'
import { Button } from '@/components/ui/button'

export function CreateIssueDialog() {
  const [open, setOpen] = useState(false)

  return (
    <>
      <Button size="sm" onClick={() => setOpen(true)}>
        <Plus />
        New issue
      </Button>
      <IssueFormDialog open={open} onOpenChange={setOpen} />
    </>
  )
}
