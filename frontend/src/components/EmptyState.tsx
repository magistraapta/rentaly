import React from 'react';

interface EmptyStateProps {
  icon?: React.ReactNode;
  title: string;
  description: string;
  actionButton?: {
    text: string;
    onClick: () => void;
  };
  className?: string;
}

const EmptyState: React.FC<EmptyStateProps> = ({
  icon,
  title,
  description,
  actionButton,
  className = ''
}) => {
  const defaultIcon = (
    <svg 
      className="mx-auto h-16 w-16 text-gray-400" 
      fill="none" 
      viewBox="0 0 24 24" 
      stroke="currentColor"
    >
      <path 
        strokeLinecap="round" 
        strokeLinejoin="round" 
        strokeWidth={1} 
        d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" 
      />
    </svg>
  );

  return (
    <div className={`text-center py-16 px-4 ${className}`}>
      <div className="max-w-md mx-auto">
        {icon || defaultIcon}
        
        <h3 className="mt-6 text-xl font-semibold text-gray-900">
          {title}
        </h3>
        
        <p className="mt-3 text-gray-500 leading-relaxed">
          {description}
        </p>
        
        {actionButton && (
          <div className="mt-8">
            <button
              onClick={actionButton.onClick}
              className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors duration-200"
            >
              {actionButton.text}
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default EmptyState;