export function LoadingState(){return <div className="empty">Loading tasks...</div>}
export function EmptyState({onReset}:{onReset:()=>void}){return <div className="empty"><p>No tasks match this view.</p><button className="secondary" onClick={onReset}>Clear filters</button></div>}
