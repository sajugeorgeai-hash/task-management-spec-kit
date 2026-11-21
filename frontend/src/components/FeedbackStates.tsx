export function LoadingFeedback(){return <p className="feedback" role="status">Loading...</p>}
export function ErrorFeedback({message}:{message:string}){return <div className="alert" role="alert">{message}</div>}
