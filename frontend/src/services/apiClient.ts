const baseUrl=import.meta.env.VITE_API_URL||'http://localhost:8080'
export async function api<T>(path:string,options:RequestInit={}):Promise<T>{
  const response=await fetch(`${baseUrl}${path}`,{...options,headers:{'Content-Type':'application/json',...options.headers}})
  if(!response.ok){
    const body=await response.json().catch(()=>({message:'Request failed'}))
    throw new Error(body.message||'Request failed')
  }
  if(response.status===204)return undefined as T
  return response.json()
}
