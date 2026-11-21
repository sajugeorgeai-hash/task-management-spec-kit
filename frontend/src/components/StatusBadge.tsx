import type {TaskStatus} from '../services/taskTypes'
export default function StatusBadge({status}:{status:TaskStatus}){return <span className={`badge status-${status.toLowerCase()}`}>{status.replace('_',' ')}</span>}
