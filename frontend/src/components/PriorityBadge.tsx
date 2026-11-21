import type {Priority} from '../services/taskTypes'
export default function PriorityBadge({priority}:{priority:Priority}){return <span className={`badge priority-${priority.toLowerCase()}`}>{priority}</span>}
