import { Order } from "@/app/type/Order"

export default function OrderCard({ order }: { order: Order }) {
    const statusStyles: Record<string, string> = {
        Pending: "bg-amber-50 text-amber-700 ring-amber-200",
        Paid: "bg-emerald-50 text-emerald-700 ring-emerald-200",
        Cancelled: "bg-rose-50 text-rose-700 ring-rose-200",
        Default: "bg-gray-100 text-gray-700 ring-gray-200"
    }

    const statusClass = statusStyles[order.status] || statusStyles.Default
    const dateFormatter = new Intl.DateTimeFormat(undefined, { year: 'numeric', month: 'short', day: '2-digit' })
    const currencyFormatter = new Intl.NumberFormat(undefined, { style: 'currency', currency: 'USD' })

    return (
        <div key={order.id} className="rounded-xl border border-gray-200 mb-4 bg-white p-5 shadow-sm transition hover:shadow-md dark:border-neutral-800 dark:bg-neutral-900">
            <div className="flex items-start justify-between gap-3">
                <div>
                    <h2 className="text-xl font-semibold tracking-tight text-gray-900 dark:text-gray-100">{order.car}</h2>
                    <p className="mt-1 text-sm text-gray-500">Order #{order.id}</p>
                </div>
                <span className={`inline-flex items-center rounded-full px-2.5 py-1 text-xs font-medium ring-1 ring-inset ${statusClass}`}>
                    {order.status}
                </span>
            </div>

            <div className="mt-4 grid grid-cols-1 gap-4 sm:grid-cols-3">
                <div className="rounded-lg bg-gray-50 p-3 dark:bg-neutral-800">
                    <p className="text-xs uppercase tracking-wide text-gray-500">Start date</p>
                    <p className="mt-1 text-sm font-medium text-gray-900 dark:text-gray-100">{dateFormatter.format(order.startDate)}</p>
                </div>
                <div className="rounded-lg bg-gray-50 p-3 dark:bg-neutral-800">
                    <p className="text-xs uppercase tracking-wide text-gray-500">End date</p>
                    <p className="mt-1 text-sm font-medium text-gray-900 dark:text-gray-100">{dateFormatter.format(order.endDate)}</p>
                </div>
                <div className="rounded-lg bg-gray-50 p-3 dark:bg-neutral-800">
                    <p className="text-xs uppercase tracking-wide text-gray-500">Total</p>
                    <p className="mt-1 text-lg font-semibold text-gray-900 dark:text-gray-100">{currencyFormatter.format(order.totalPrice)}</p>
                </div>
            </div>

            <div className="mt-4 flex items-center justify-end gap-3">
                <button className="inline-flex items-center rounded-lg border border-gray-200 px-3 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 dark:border-neutral-700 dark:text-gray-200 dark:hover:bg-neutral-800">
                    View details
                </button>
                <button className="inline-flex items-center rounded-lg bg-gray-900 px-3 py-2 text-sm font-medium text-white hover:bg-black/90 dark:bg-white dark:text-black dark:hover:bg-white/90">
                    Invoice
                </button>
            </div>
        </div>
    )
}

