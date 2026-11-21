// @vitest-environment jsdom
import {render,screen} from '@testing-library/react'
import '@testing-library/jest-dom/vitest'
import {describe,it,expect} from 'vitest'
import StatusBadge from '../src/components/StatusBadge'
import PriorityBadge from '../src/components/PriorityBadge'
import TaskIllustration from '../src/components/TaskIllustration'

describe('task state presentation',()=>{
 it('renders status and priority badges',()=>{
  render(<><StatusBadge status="IN_PROGRESS"/><PriorityBadge priority="HIGH"/></>)
  expect(screen.getByText('IN PROGRESS')).toBeInTheDocument()
  expect(screen.getByText('HIGH')).toBeInTheDocument()
 })

 it('renders colorful task artwork',()=>{
  const task={id:'task-1',title:'Design',status:'IN_PROGRESS' as const,priority:'HIGH' as const,createdAt:'',updatedAt:''}
  render(<TaskIllustration task={task}/>)
  expect(document.querySelector('.task-illustration')).toBeInTheDocument()
 })
})
