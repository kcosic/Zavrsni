using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.Exceptions;
using WebAPI.Models.Helpers;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class ShopController : BaseController
    {
        public ShopController() : base(nameof(ShopController)) { }

        [HttpGet]
        [Route("api/Shop/{id}")]
        public BaseResponse RetrieveShop(int id, bool expanded = false)
        {
            try
            {
                var shop = Db.Shops.Find(id);
                if (shop == null || shop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(shop.ToDTO(expanded ? 3 : 2));
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
        [Route("api/Shop")]
        public BaseResponse RetrieveShops()
        {
            try
            {
                var shops = Db.Shops.Where(x => !x.Deleted).ToList();
                if (shops == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Shop.ToListDTO(shops, 2));
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
        [Route("api/Shop/{id}/Availability/{dateString}")]
        public BaseResponse RetrieveAvailabilityForShop(int id, string dateString)
        {
            try
            {
                if(dateString == null)
                {
                    throw new ArgumentNullException();
                }

                var splitDate = Helper.FromBase64(dateString).Split('.');
                if(splitDate.Length != 3)
                {
                    throw ArgumentOutOfRangeException();
                }

                var date = new DateTime(int.Parse(splitDate[2]), int.Parse(splitDate[1]), int.Parse(splitDate[0]));

                var shop = Db.Shops.Find(id);
                if (shop == null)
                {
                    throw new RecordNotFoundException();
                }

                var takenAppointments = shop.Appointments
                    .Where(x => x.DateTimeStart.Date >= date.Date && date.Date >= x.DateTimeEnd.Date)
                    .ToList();

                var takenDates = new List<string>();

                var workHours = shop.WorkHours.Split('-');
                var start = int.Parse(workHours[1].Split(':')[1]);
                var end = int.Parse(workHours[1].Split(':')[0]);

                if(takenAppointments.Count >= shop.CarCapacity)
                {
                    takenAppointments.ForEach(appt =>
                    {
                        if (appt.DateTimeEnd == date.Date)
                        {
                            for (int hour = start; hour < appt.DateTimeEnd.Hour; hour++)
                            {
                                takenDates.Add($"{(hour < 10 ? "0" : "")}{hour}:00");
                            }
                        }
                        else if (appt.DateTimeStart == date.Date)
                        {
                            for (int hour = appt.DateTimeStart.Hour; hour < end; hour++)
                            {
                                takenDates.Add($"{(hour < 10 ? "0" : "")}{hour}:00");
                            }
                        }
                        else
                        {
                            for (int hour = start; hour < end; hour++)
                            {
                                takenDates.Add($"{(hour < 10 ? "0" : "")}{hour}:00");
                            }
                        }
                    });
                }
                return CreateOkResponse(takenDates);
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

        private Exception ArgumentOutOfRangeException()
        {
            throw new NotImplementedException();
        }

        [HttpGet]
        [Route("api/Shop/{id}/Childs")]
        public BaseResponse RetrieveChildShops(int id)
        {
            try
            {
                var parentShop = Db.Shops.Find(id);
                if (parentShop == null || parentShop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var childShops = parentShop.ChildShops.Where(x => !x.Deleted).ToList();
                if (childShops == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Shop.ToListDTO(childShops, 2));
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
        [Route("api/Shop/{id}/Parent")]
        public BaseResponse RetrieveParentShop(int id)
        {
            try
            {
                var childShop = Db.Shops.Find(id);
                if (childShop == null || childShop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var parentShop = childShop.ParentShop;
                if (parentShop == null || parentShop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(parentShop);
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

        /// <summary>
        /// Endpoint for retrieving <see cref="Shop"/> <see cref="Review"/>s
        /// </summary>
        /// <param name="id">Identifier of the <see cref="Shop"/></param>
        /// <returns></returns>
        [HttpGet]
        [Route("api/Shop/{id}/Reviews")]
        public BaseResponse RetrieveShopReviews(int id)
        {
            try
            {
                var shop = Db.Shops.Find(id);
                if (shop == null || shop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var shopReviews = shop.Reviews.Where(x=> !x.Deleted).ToList();
                if (shopReviews == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(shopReviews);
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

        /// <summary>
        /// Endpoint for retrieving <see cref="Shop"/> <see cref="Review"/>s
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [Route("api/Shop/Reviews/Recent")]
        public BaseResponse RetrieveRecentShopReviews()
        {
            try
            {
                var shop = Db.Shops.Find(AuthShop.Id);
                if (shop == null || shop.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var shopReviews = shop.Reviews.Where(x=> !x.Deleted).OrderByDescending(x=> x.DateCreated).Take(5).ToList();
                if (shopReviews == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Review.ToListDTO(shopReviews,1));
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

        /// <summary>
        /// Endpoint for retrieving <see cref="Shop"/> <see cref="Review"/>s
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [Route("api/Shop/NotificationData")]
        public BaseResponse RetrieveShopNotificationData()
        {
            try
            {
                var shop = Db.Shops.Find(AuthShop.Id);
                if (shop == null || shop.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                var notificationData = new ShopNotificationData
                {
                    NewRequests = shop.Requests.Where(x => !x.ShopAccepted.HasValue && !x.UserAccepted.HasValue && !x.Deleted).ToList().Select(x => x.Id).ToList(),
                    UpdatedRequests = shop.Requests.Where(x => x.ShopAccepted.HasValue && !x.UserAccepted.HasValue && !x.Deleted).ToList().Select(x => x.Id).ToList()
                };

                return CreateOkResponse(notificationData);
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

        /// <summary>
        /// Endpoint for deleting the <see cref="Shop"/>
        /// </summary>
        /// <param name="id">Identifier of the <see cref="Shop"/></param>
        /// <returns></returns>
        [HttpDelete]
        [Route("api/Shop/{id}")]
        public BaseResponse DeleteShop(int id)
        {
            try
            {
                var shop = Db.Shops.Find(id);
                if (shop == null || shop.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                shop.DateDeleted = DateTime.Now;
                shop.Deleted = true;
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


        /// <summary>
        /// Endpoint for updating <see cref="Shop"/>.
        /// </summary>
        /// <param name="id">Identifier of the <see cref="Shop"/></param>
        /// <param name="shopDTO"><see cref="Shop"/> object with modified data</param>
        /// <returns></returns>
        [HttpPut]
        [Route("api/Shop/{id}")]
        public BaseResponse UpdateShop([FromUri] int id, [FromBody] ShopDTO shopDTO)
        {
            try
            {
                if (shopDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var shop = Db.Shops.Find(id);
                if (shop == null || shop.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                if (shop.Email != shopDTO.Email && Db.Shops.Where(x => x.Email == shopDTO.Email).Count() > 0)
                {
                    throw new Exception("Email is taken");
                }
                if (shop.LegalName != shopDTO.LegalName && Db.Shops.Where(x => x.LegalName == shopDTO.LegalName).Count() > 0)
                {
                    throw new Exception("Legal name is taken");
                }
                if (shop.ShortName != shopDTO.ShortName && Db.Shops.Where(x => x.ShortName == shopDTO.ShortName).Count() > 0)
                {
                    throw new Exception("Short name is taken");
                }
                shop.DateModified = DateTime.Now;
                shop.Email = shopDTO.Email;
                shop.LegalName = shopDTO.LegalName;
                shop.LocationId = shopDTO.LocationId;
                shop.ParentShopId = shopDTO.ParentShopId;
                shop.Vat = shopDTO.Vat;
                shop.WorkDays = shopDTO.WorkDays;
                shop.WorkHours = shopDTO.WorkHours;
                shop.CarCapacity = shopDTO.CarCapacity;
                shop.HourlyRate = shopDTO.HourlyRate;

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
    }
}
