export type TaskStatus='TODO'|'IN_PROGRESS'|'DONE'
export type Priority='LOW'|'MEDIUM'|'HIGH'
export interface User{ id:string; displayName:string; email:string }
export interface TaskItem{id:string;title:string;description?:string;status:TaskStatus;priority:Priority;dueDate?:string;assignee?:User;createdAt:string;updatedAt:string}
export interface PagedTasks{content:TaskItem[];page:number;size:number;totalElements:number;totalPages:number;first:boolean;last:boolean}
