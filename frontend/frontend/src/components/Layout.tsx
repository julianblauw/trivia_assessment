import type { ReactNode } from "react";

export default function Layout({ children }: { children: ReactNode }) {
    return (
        <div className='bg-[#FAF8FF] h-screen p-25'>
            <div className="flex flex-col items-center">
                {children}
            </div>
        </div>
    )
}