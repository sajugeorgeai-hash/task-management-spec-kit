import {api} from './apiClient'
import type {PagedTasks,TaskItem,User} from './taskTypes'

export type TaskInput={title:string;description?:string;status:string;priority:string;dueDate?:string;assigneeId?:string}
export function listTasks(params:URLSearchParams):Promise<PagedTasks>{return api<PagedTasks>(`/api/tasks?${params}`)}
export function getUsers():Promise<User[]>{return api<User[]>('/api/users')}
export function createTask(body:TaskInput):Promise<TaskItem>{return api<TaskItem>('/api/tasks',{method:'POST',body:JSON.stringify(body)})}
export function updateTask(id:string,body:TaskInput):Promise<TaskItem>{return api<TaskItem>(`/api/tasks/${id}`,{method:'PUT',body:JSON.stringify(body)})}
export function deleteTask(id:string):Promise<void>{return api<void>(`/api/tasks/${id}`,{method:'DELETE'})}
