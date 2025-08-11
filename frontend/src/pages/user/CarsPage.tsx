import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import type { Car, CarType } from '../../types/api';
import EmptyState from '../../components/EmptyState';

const CarsPage: React.FC = () => {
  const [cars, setCars] = useState<Car[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedType, setSelectedType] = useState<CarType | ''>('');

  useEffect(() => {
    fetchCars();
  }, []);

  const fetchCars = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await apiService.getAllCars();
      setCars(response.data);
    } catch (err) {
      setError('Failed to fetch cars. Please try again later.');
      console.error('Error fetching cars:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchCarsByType = async (type: CarType) => {
    try {
      setLoading(true);
      setError(null);
      const response = await apiService.getCarsByType(type);
      setCars(response.data);
    } catch (err) {
      setError(`Failed to fetch ${type} cars. Please try again later.`);
      console.error('Error fetching cars by type:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleTypeFilter = (type: CarType | '') => {
    setSelectedType(type);
    if (type === '') {
      fetchCars();
    } else {
      fetchCarsByType(type);
    }
  };

  const getDisplayType = (carType: CarType): string => {
    return carType.charAt(0).toUpperCase() + carType.slice(1);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-800 mb-4">Available Cars</h1>
          <p className="text-gray-600">Choose from our selection of quality rental vehicles</p>
        </div>
        
        {/* Filter Section */}
        <div className="bg-white rounded-lg shadow-md p-4 mb-6">
          <h2 className="text-lg font-semibold mb-3">Filters</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Car Type</label>
              <select 
                value={selectedType} 
                onChange={(e) => handleTypeFilter(e.target.value as CarType | '')}
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">All Types</option>
                <option value="sedan">Sedan</option>
                <option value="suv">SUV</option>
                <option value="truck">Truck</option>
              </select>
            </div>
            
            
          </div>
        </div>
        
        {/* Loading State */}
        {loading && (
          <div className="flex justify-center items-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            <span className="ml-3 text-gray-600">Loading cars...</span>
          </div>
        )}

        {/* Error State */}
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-8">
            <div className="flex">
              <div className="flex-shrink-0">
                <svg className="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                </svg>
              </div>
              <div className="ml-3">
                <p>{error}</p>
                <button 
                  onClick={fetchCars}
                  className="mt-2 text-sm bg-red-600 hover:bg-red-700 text-white px-3 py-1 rounded transition-colors duration-200"
                >
                  Try Again
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Cars Grid */}
        {!loading && !error && (
          <>
            {cars.length === 0 ? (
              <EmptyState
                icon={
                  <svg className="mx-auto h-16 w-16 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M8.25 18.75a1.5 1.5 0 01-3 0V8.25a1.5 1.5 0 011.5-1.5h4.5a1.5 1.5 0 011.5 1.5v10.5m0 0h3.75a1.5 1.5 0 001.5-1.5V4.875c0-.621-.504-1.125-1.125-1.125H10.125c-.621 0-1.125.504-1.125 1.125v14.25a1.5 1.5 0 01-1.5 1.5z" />
                  </svg>
                }
                title="No cars available"
                description={selectedType ? `No ${selectedType} cars found. Try adjusting your filters or browse other vehicle types.` : "No cars are currently available. Please check back later or contact our support team."}
                actionButton={{
                  text: "Refresh Cars",
                  onClick: fetchCars
                }}
              />
            ) : (
              <div className="grid grid-cols-4 gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                {cars.map((car, index) => (
                  <div key={`${car.name}-${index}`} className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-200">
                    <div className="relative">
                        <img 
                          src={`https://picsum.photos/200/300?random=${index}`} 
                          alt={car.name} 
                          className="w-full h-40 object-cover"
                          onError={(e) => {
                            const target = e.target as HTMLImageElement;
                            target.src = `https://via.placeholder.com/300x200?text=${encodeURIComponent(car.name)}`;
                          }}
                        />
                      <span className="absolute top-2 right-2 bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full font-medium">
                        {getDisplayType(car.carType)}
                      </span>
                    </div>
                    <div className="p-4">
                      <div className="mb-3">
                        <h3 className="text-lg font-semibold text-gray-800 truncate">{car.name}</h3>
                        <div className="flex items-center mt-1">
                          <span className="text-xl font-bold text-green-600">${car.price}</span>
                          <span className="text-gray-500 text-sm ml-1">/day</span>
                        </div>
                      </div>
                      {car.description && (
                        <div className="mb-3">
                          <p className="text-xs text-gray-600 line-clamp-2">{car.description}</p>
                        </div>
                      )}
                      <Link
                        to={`/cars/${index + 1}`}
                        className="block w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-3 rounded-md transition-colors duration-200 text-sm text-center"
                      >
                        View Details
                      </Link>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default CarsPage;