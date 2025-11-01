"use client"

import * as React from "react"
import { useForm } from "react-hook-form"
import { Button } from "@/components/ui/button"
import { login } from "@/app/api/auth"
import { LoginRequest } from "@/app/data/auth"


export default function LoginForm() {
  const [isSubmitting, setIsSubmitting] = React.useState(false)

  const form = useForm<LoginRequest>({
    defaultValues: { username: "", password: "" },
    mode: "onBlur",
  })

  async function onSubmit(values: LoginRequest) {
    try {
      setIsSubmitting(true)
      await login(values)
    } finally {
      setIsSubmitting(false)
    }
  }

  const { register, handleSubmit, formState: { errors } } = form

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="flex flex-col gap-1.5">
        <label htmlFor="username" className="text-sm font-medium text-gray-700">Username</label>
        <input
          id="username"
          type="text"
          autoComplete="username"
          className="w-full rounded-md border border-gray-300 bg-white px-3 py-2 text-sm outline-none focus:ring-[3px] focus:ring-ring/50 focus:border-ring"
          placeholder="Enter your username"
          {...register("username")}
        />
        {errors.username && (
          <p className="text-xs text-red-600">{errors.username.message}</p>
        )}
      </div>

      <div className="flex flex-col gap-1.5">
        <label htmlFor="password" className="text-sm font-medium text-gray-700">Password</label>
        <input
          id="password"
          type="password"
          autoComplete="current-password"
          className="w-full rounded-md border border-gray-300 bg-white px-3 py-2 text-sm outline-none focus:ring-[3px] focus:ring-ring/50 focus:border-ring"
          placeholder="Enter your password"
          {...register("password")}
        />
        {errors.password && (
          <p className="text-xs text-red-600">{errors.password.message}</p>
        )}
      </div>

      <Button type="submit" className="w-full" disabled={isSubmitting}>
        {isSubmitting ? "Signing in…" : "Sign in"}
      </Button>
    </form>
  )
}