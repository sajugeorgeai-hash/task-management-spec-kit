import type {TaskItem} from '../services/taskTypes'
import StatusBadge from './StatusBadge';import PriorityBadge from './PriorityBadge';import DueDateLabel from './DueDateLabel'
export default function TaskDetail({task,onEdit}:{task:TaskItem;onEdit:(t:TaskItem)=>void}){return <aside className="task-detail"><h2>{task.title}</h2><div><StatusBadge status={task.status}/><PriorityBadge priority={task.priority}/></div><p>{task.description||'No description'}</p><DueDateLabel dueDate={task.dueDate}/><button className="secondary" onClick={()=>onEdit(task)}>Edit</button></aside>}
