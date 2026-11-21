import {useEffect,useState} from 'react'
import type {Priority,TaskItem,TaskStatus,User} from '../services/taskTypes'
import {createTask,deleteTask,getUsers,listTasks,updateTask} from '../services/taskApi'
import TaskCard from '../components/TaskCard'
import TaskTable from '../components/TaskTable'
import SearchBar from '../components/SearchBar'
import FilterPanel from '../components/FilterPanel'
import PaginationControls from '../components/PaginationControls'
import SortControls from '../components/SortControls'
import TaskForm from '../components/TaskForm'
import DeleteTaskDialog from '../components/DeleteTaskDialog'

export default function Dashboard(){
 const [tasks,setTasks]=useState<TaskItem[]>([]);const [users,setUsers]=useState<User[]>([])
 const [search,setSearch]=useState('');const [status,setStatus]=useState<TaskStatus|''>('');const [priority,setPriority]=useState<Priority|''>('')
 const [sort,setSort]=useState('createdAt');const [direction,setDirection]=useState<'asc'|'desc'>('desc')
 const [page,setPage]=useState(0);const [size,setSize]=useState(6);const [totalPages,setTotalPages]=useState(0)
 const [view,setView]=useState<'cards'|'table'>('cards');const [editing,setEditing]=useState<TaskItem|null>(null)
 const [creating,setCreating]=useState(false);const [deleting,setDeleting]=useState<TaskItem|null>(null);const [error,setError]=useState('')
 async function load(p=page){
  setError('')
  const params=new URLSearchParams({page:String(p),size:String(size),sort,direction})
  if(search)params.set('search',search);if(status)params.set('status',status);if(priority)params.set('priority',priority)
  try{const data=await listTasks(params);setTasks(data.content);setPage(data.page);setTotalPages(data.totalPages)}
  catch(e){setError(e instanceof Error?e.message:'Unable to load tasks')}
 }
 useEffect(()=>{getUsers().then(setUsers).catch(e=>setError(e.message));load(0)},[search,status,priority,sort,direction,size])
 async function save(input:any){
  try{editing?await updateTask(editing.id,input):await createTask(input);setEditing(null);setCreating(false);await load(0)}
  catch(e){setError(e instanceof Error?e.message:'Unable to save task')}
 }
 async function remove(){
  if(!deleting)return
  try{await deleteTask(deleting.id);setDeleting(null);await load(page)}catch(e){setError(e instanceof Error?e.message:'Unable to delete task')}
 }
 return <main className="app-shell">
  <header><div><p className="eyebrow">Workspace</p><h1>Task Dashboard</h1></div><button className="primary" onClick={()=>setCreating(true)}>+ New Task</button></header>
  {error&&<div className="alert" role="alert">{error}</div>}
  <section className="toolbar"><SearchBar value={search} onChange={setSearch}/><FilterPanel status={status} priority={priority} onStatus={setStatus} onPriority={setPriority}/>
   <SortControls sort={sort} direction={direction} size={size} onSort={setSort} onDirection={setDirection} onSize={setSize}/>
   <div className="view-toggle" aria-label="View mode"><button className={view==='cards'?'active':''} onClick={()=>setView('cards')}>Cards</button><button className={view==='table'?'active':''} onClick={()=>setView('table')}>Table</button></div>
  </section>
  {tasks.length===0?<div className="empty">No tasks match this view.</div>:view==='cards'?<div className="task-grid">{tasks.map(t=><TaskCard key={t.id} task={t} onEdit={setEditing} onDelete={setDeleting}/>)}</div>:<TaskTable tasks={tasks} onEdit={setEditing} onDelete={setDeleting}/>}
  <PaginationControls page={page} totalPages={totalPages} onPage={p=>load(p)}/>
  {(creating||editing)&&<TaskForm users={users} task={editing} onSave={save} onClose={()=>{setCreating(false);setEditing(null)}}/>}
  {deleting&&<DeleteTaskDialog task={deleting} onConfirm={remove} onClose={()=>setDeleting(null)}/>}
 </main>
}
