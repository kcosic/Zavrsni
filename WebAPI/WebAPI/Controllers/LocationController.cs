using System;
using System.Buffers.Text;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.Helpers;
using WebAPI.Models.HERE;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class LocationController : BaseController
    {
        public LocationController() : base(nameof(LocationController)) { }


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
        [Route("api/Location/Address/{address}")]
        public BaseResponse RetrieveLocationByAddress(string address)
        {
            try
            {
                if (address == null || address.Length == 0)
                {
                    throw new ArgumentException("Invalid address");
                }
                var service = HereService.getInstance();
                var locationDTO = service.RetrieveLocationDTOInformation(address);
                return CreateOkResponse(locationDTO);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.InvalidParameter);
            }
        }


        [AllowAnonymous]
        [HttpGet]
        [Route("api/Location/Discover/{base64Address}")]
        public BaseResponse DiscoverLocationByAddress(string base64Address)
        {
            try
            {
                var address = Helper.FromBase64(base64Address);
                if (address == null || address.Length == 0)
                {
                    throw new ArgumentException("Invalid address");
                }
                var service = HereService.getInstance();
                var locationsDTO = service.DiscoverLocationDTOByAddress(address);
                return CreateOkResponse(locationsDTO);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.InvalidParameter);
            }
        }
    }
}
