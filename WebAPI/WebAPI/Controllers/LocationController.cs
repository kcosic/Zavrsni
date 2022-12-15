using System;
using System.Buffers.Text;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.Exceptions;
using WebAPI.Models.Helpers;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;
using WebAPI.Services.HERE;

namespace WebAPI.Controllers
{
    public class LocationController : BaseController
    {
        public LocationController() : base(nameof(LocationController)) { }


        [HttpGet]
        [Route("api/Location/{id}")]
        public BaseResponse RetrieveLocation(int id, bool expanded = false)
        {
            try
            {
                var location = Db.Locations.Find(id);
                if (location == null || location.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(location.ToDTO(expanded ? 3 : 2));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/Location")]
        public BaseResponse RetrieveLocations()
        {
            try
            {
                var location = Db.Locations.Where(x => x.UserId == AuthUser.Id && !x.Deleted).ToList();
                if (location == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Location.ToListDTO(location, 2));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpDelete]
        [Route("api/Location/{id}")]
        public BaseResponse DeleteLocation(int id)
        {
            try
            {
                var location = Db.Locations.Find(id);
                if (location == null || location.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                location.DateDeleted = DateTime.Now;
                location.Deleted = true;
                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPost]
        [Route("api/Location")]
        public BaseResponse CreateLocation(LocationDTO newLocationDTO)
        {
            try
            {
                if (newLocationDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var newLocation = new Location
                {
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    UserId = newLocationDTO.UserId,
                    City = newLocationDTO.City,
                    Country = newLocationDTO.Country,
                    County = newLocationDTO.County,
                    Latitude = newLocationDTO.Latitude,
                    Longitude = newLocationDTO.Longitude,
                    Street = newLocationDTO.Street,
                    StreetNumber = newLocationDTO.StreetNumber
                };

                Db.Locations.Add(newLocation);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/Location/{id}")]
        public BaseResponse UpdateLocation([FromUri] int id, [FromBody] LocationDTO locationDTO)
        {
            try
            {
                if (locationDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var location = Db.Locations.Find(id);
                if (location == null || location.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                location.DateModified = DateTime.Now;
                location.UserId = locationDTO.UserId;
                location.City = locationDTO.City;
                location.Country = locationDTO.Country;
                location.County = locationDTO.County;
                location.Latitude = locationDTO.Latitude;
                location.Longitude = locationDTO.Longitude;
                location.Street = locationDTO.Street;
                location.StreetNumber = locationDTO.StreetNumber;

                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }


        [HttpGet]
        [Route("api/Location/Coordinates/{encodedLatLng}/Radius/{radius}")]
        public BaseResponse RetrieveLocationByCoordinates(string encodedLatLng, int radius, DateTime? date = null)
        {
            try
            {
                var latLng = Helper.FromBase64(encodedLatLng);
                string[] latLngSplit = latLng.Split('!');
                string rawLat = latLngSplit[0];
                string rawLng = latLngSplit[1];

                double lat;
                double lng;

                if (!double.TryParse(rawLat, out lat) || !double.TryParse(rawLng, out lng))
                {
                    throw new ArgumentException("Invalid latitude or longitude");
                }

                List<Location> locations;

                var tempLocation = new Location { Latitude = lat, Longitude = lng };

                locations = Db.Locations.Where(x =>
                        x.UserId == null &&
                        !x.Deleted
                    )
                    .ToList();

                locations = locations.Where(x => Helper.CalculateDistanceBetweenPoints(x, tempLocation) <= radius)
                    .ToList();

                if (date != null)
                {

                    locations = locations.Where(x => x.Shops.Count() > 0 && Helper.IsDateInWorkday(x.Shops.First().WorkDays, date.Value)).ToList();
                }

                return CreateOkResponse(Location.ToListDTO(locations, 3));
            }
            catch (ArgumentException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.InvalidParameter);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [AllowAnonymous]
        [HttpGet]
        [Route("api/Location/Coordinates/{encodedLatLng}")]
        public BaseResponse RetrieveLocationByCoordinates(string encodedLatLng)
        {
            try
            {
                var latLng = Helper.FromBase64(encodedLatLng);
                string[] latLngSplit = latLng.Split('!');
                string rawLat = latLngSplit[0];
                string rawLng = latLngSplit[1];

                double lat;
                double lng;

                if (!double.TryParse(rawLat, out lat) || !double.TryParse(rawLng, out lng))
                {
                    throw new ArgumentException("Invalid latitude or longitude");
                }
                var service = HereService.getInstance();


                var locationDTO = service.RetrieveLocationDTOInformation(lat, lng);


                return CreateOkResponse(locationDTO);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.InvalidParameter);
            }

        }

        [AllowAnonymous]
        [HttpGet]
        [Route("api/Location/Address/{encodedAddress}")]
        public BaseResponse RetrieveLocationByAddress(string encodedAddress)
        {
            try
            {
                var address = Helper.FromBase64(encodedAddress);
                if (address == null || address.Length == 0)
                {
                    throw new ArgumentException("Invalid address");
                }
                var service = HereService.getInstance();
                var locationDTO = service.RetrieveLocationDTOInformation(address);
                return CreateOkResponse(locationDTO);
            }
            catch (LocationNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.InvalidParameter);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }


        [AllowAnonymous]
        [HttpGet]
        [Route("api/Location/Discover/{encodedAddress}")]
        public BaseResponse DiscoverLocationByAddress(string encodedAddress)
        {
            try
            {
                var address = Helper.FromBase64(encodedAddress);
                if (address == null || address.Length == 0)
                {
                    throw new ArgumentException("Invalid address");
                }
                var service = HereService.getInstance();
                var locationsDTO = service.DiscoverLocationDTOByAddress(address);
                return CreateOkResponse(locationsDTO);
            }
            catch (LocationNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.InvalidParameter);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }
    }
}
