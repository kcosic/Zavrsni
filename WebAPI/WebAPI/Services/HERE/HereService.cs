using Newtonsoft.Json;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using WebAPI.Models.DTOs;
using WebAPI.Models.Exceptions;
using WebAPI.Services.Models.HERE;
using WebAPI.Services.Models.Responses.HERE;

namespace WebAPI.Services.HERE
{
    public class HereService
    {
        private static HereService hereService = null;
        public static HereService getInstance()
        {
            if (hereService == null)
            {
                hereService = new HereService();
            }
            return hereService;
        }
        private HereService() { }


        /// <summary>
        /// Retrieves location information using latitude and longitude
        /// </summary>
        /// <param name="latitude"></param>
        /// <param name="longitude"></param>
        /// <returns><see cref="Location"></returns>
        public WebAPI.Models.ORM.Location RetrieveLocationInformation(double latitude, double longitude)
        {
            WebAPI.Models.ORM.Location location;

            using (HttpClient client = new HttpClient())
            {
                var response = client.GetAsync($"https://revgeocode.search.hereapi.com/v1/revgeocode?at={latitude},{longitude}&apiKey=b0ZYVFwkO821z1uYL8GAEQgJmxSaOOrEGEWR1xk9BN4")?.Result;
                var locationResponse = JsonConvert.DeserializeObject<LocationResponse>(response.Content.ReadAsStringAsync().Result);

                if (locationResponse.Items?.Length == 0)
                {
                    throw new LocationNotFoundException();
                }

                var hereLocation = locationResponse.Items[0];

                location = new WebAPI.Models.ORM.Location
                {
                    Street = hereLocation.Address?.Street,
                    City = hereLocation.Address?.City,
                    Country = hereLocation.Address?.CountryName,
                    County = hereLocation.Address?.County,
                    Latitude = hereLocation.Position?.Latitude != null ? double.Parse(hereLocation.Position?.Latitude) : latitude,
                    Longitude = hereLocation.Position?.Longitude != null ? double.Parse(hereLocation.Position?.Longitude) : longitude,
                    StreetNumber = hereLocation.Address?.HouseNumber,
                };
            }

            return location;
        }


        /// <summary>
        /// Retrieves location information using latitude and longitude
        /// </summary>
        /// <param name="latitude"></param>
        /// <param name="longitude"></param>
        /// <returns><see cref="LocationDTO"></returns>
        public LocationDTO RetrieveLocationDTOInformation(double latitude, double longitude)
        {
            return RetrieveLocationInformation(latitude, longitude).ToDTO();
        }

        public List<WebAPI.Models.ORM.Location> DiscoverLocationByAddress(string address)
        {
            List<WebAPI.Models.ORM.Location> locations = new List<WebAPI.Models.ORM.Location>();

            using (HttpClient client = new HttpClient())
            {
                var response = client.GetAsync($"https://autocomplete.search.hereapi.com/v1/autocomplete?q={address}&limit=5&apiKey=b0ZYVFwkO821z1uYL8GAEQgJmxSaOOrEGEWR1xk9BN4")?.Result;
                var locationResponse = JsonConvert.DeserializeObject<LocationResponse>(response.Content.ReadAsStringAsync().Result);

                if (locationResponse.Items?.Length == 0)
                {
                    throw new LocationNotFoundException();
                }
                List<Location> filteredLocations = new List<Location>();

                
                foreach (var hereLocation in locationResponse.Items)
                {
                    if (hereLocation.ResultType == "houseNumber")
                    {
                        locations.Add(RetrieveLocationInformation(hereLocation.Address.Label));
                    }
                    else
                    {
                        locations.Add(new WebAPI.Models.ORM.Location
                        {
                            Street = hereLocation.Address?.Street,
                            City = hereLocation.Address?.City,
                            Country = hereLocation.Address?.CountryName,
                            County = hereLocation.Address?.County,
                            StreetNumber = hereLocation.Address?.HouseNumber
                        });
                    }

                }

            }

            return locations;
        }
        public List<LocationDTO> DiscoverLocationDTOByAddress(string address)
        {
            return WebAPI.Models.ORM.Location.ToListDTO(DiscoverLocationByAddress(address))?.ToList();

        }


        /// <summary>
        /// Retrieves location information using address
        /// </summary>
        /// <param name="address"></param>
        /// <returns><see cref="Location"></returns>
        public WebAPI.Models.ORM.Location RetrieveLocationInformation(string address)
        {
            WebAPI.Models.ORM.Location location;

            using (HttpClient client = new HttpClient())
            {
                var response = client.GetAsync($"https://geocode.search.hereapi.com/v1/geocode?q={address}&apiKey=b0ZYVFwkO821z1uYL8GAEQgJmxSaOOrEGEWR1xk9BN4")?.Result;
                var locationResponse = JsonConvert.DeserializeObject<LocationResponse>(response.Content.ReadAsStringAsync().Result);

                if (locationResponse.Items?.Length == 0)
                {
                    throw new LocationNotFoundException();
                }

                var hereLocation = locationResponse.Items[0];

                location = new WebAPI.Models.ORM.Location
                {
                    Street = hereLocation.Address?.Street,
                    City = hereLocation.Address?.City,
                    Country = hereLocation.Address?.CountryName,
                    County = hereLocation.Address?.County,
                    Latitude = double.Parse(hereLocation.Position?.Latitude),
                    Longitude = double.Parse(hereLocation.Position?.Longitude),
                    StreetNumber = hereLocation.Address?.HouseNumber
                };
            }

            return location;
        }


        /// <summary>
        /// Retrieves location information using address
        /// </summary>
        /// <param name="address"></param>
        /// <returns><see cref="LocationDTO"></returns>
        public LocationDTO RetrieveLocationDTOInformation(string address)
        {
            return RetrieveLocationInformation(address).ToDTO();
        }
    }
}