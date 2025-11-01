export default function Loading() {
    return (
        <div className="min-h-screen flex items-center justify-center bg-zinc-50">
            <div className="flex flex-col items-center gap-3">
                <div className="h-8 w-8 rounded-full border-2 border-zinc-300 border-t-zinc-900 animate-spin" />
                <p className="text-sm text-zinc-600">Loading…</p>
            </div>
        </div>
    )
}
