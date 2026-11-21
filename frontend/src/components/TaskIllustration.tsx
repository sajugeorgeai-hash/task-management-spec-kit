import type {TaskItem} from '../services/taskTypes'

const symbols: Record<TaskItem['status'], string> = {
  TODO: '✓',
  IN_PROGRESS: '↗',
  DONE: '★'
}

export default function TaskIllustration({task}:{task:TaskItem}) {
  const hue = task.priority === 'HIGH' ? 'coral' : task.priority === 'MEDIUM' ? 'violet' : 'teal'
  return (
    <div className={`task-illustration illustration-${hue}`} aria-hidden="true">
      <svg viewBox="0 0 360 150" role="img">
        <defs>
          <linearGradient id={`sky-${task.id}`} x1="0" y1="0" x2="1" y2="1">
            <stop offset="0%" stopColor={hue === 'coral' ? '#ff9a76' : hue === 'violet' ? '#a78bfa' : '#36c6b7'} />
            <stop offset="100%" stopColor={hue === 'coral' ? '#ff6b8a' : hue === 'violet' ? '#6d5dfc' : '#38a4dd'} />
          </linearGradient>
        </defs>
        <rect width="360" height="150" rx="20" fill={`url(#sky-${task.id})`} />
        <circle cx="300" cy="34" r="44" fill="rgba(255,255,255,.18)" />
        <circle cx="55" cy="118" r="58" fill="rgba(255,255,255,.12)" />
        <rect x="86" y="31" width="188" height="98" rx="14" fill="rgba(255,255,255,.94)" />
        <rect x="108" y="54" width="144" height="12" rx="6" fill="#25324a" opacity=".75" />
        <rect x="108" y="78" width="116" height="9" rx="4.5" fill="#91a0b8" />
        <rect x="108" y="97" width="86" height="9" rx="4.5" fill="#bac4d4" />
        <circle cx="86" cy="60" r="25" fill="rgba(255,255,255,.28)" stroke="rgba(255,255,255,.8)" strokeWidth="5" />
        <text x="86" y="68" textAnchor="middle" fill="#ffffff" fontSize="26" fontWeight="800">{symbols[task.status]}</text>
      </svg>
    </div>
  )
}
