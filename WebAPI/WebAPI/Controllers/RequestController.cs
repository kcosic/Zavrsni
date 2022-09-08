using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class RequestController : BaseController
    {
        public RequestController() : base(nameof(RequestController)) { }

        [HttpGet]
        [Route("api/Request/{id}")]
        public BaseResponse RetrieveRequest(string id)
        {
            try
            {
                var request = Db.Requests.Find(id);
                if (request == null || request.Deleted)
                {
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(request.ToDTO());
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/Request")]
        public BaseResponse RetrieveRequests()
        {
            try
            {
                var request = Db.Requests.Where(x => x.ShopId == AuthShop.Id && !x.Deleted).ToList();
                if (request == null)
                {
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(Models.ORM.Request.ToListDTO(request));
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpDelete]
        [Route("api/Request/{id}")]
        public BaseResponse DeleteRequest(string id)
        {
            try
            {
                var request = Db.Requests.Find(id);
                if (request == null || request.Deleted)
                {
                    throw new Exception("Record not found");
                }
                request.DateDeleted = DateTime.Now;
                request.Deleted = true;

                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPost]
        [Route("api/Request")]
        public BaseResponse CreateRequest(Models.ORM.Request newRequestDTO)
        {
            try
            {
                throw new NotImplementedException();
                if (newRequestDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var newRequest = new Models.ORM.Request
                {
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    ShopId = newRequestDTO.ShopId,
                    ActualFinishDate = newRequestDTO.ActualFinishDate,
                    ActualPrice = newRequestDTO.ActualPrice,
                    BillPicture= newRequestDTO.BillPicture,
                    EstimatedFinishDate= newRequestDTO.EstimatedFinishDate,
                    EstimatedPrice= newRequestDTO.EstimatedPrice,
                    Price= newRequestDTO.Price,
                    UserId= newRequestDTO.UserId
                };

                Db.Requests.Add(newRequest);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/Request/{id}")]
        public BaseResponse UpdateAppointment([FromUri] string id, [FromBody] Models.ORM.Request requestDTO)
        {
            try
            {
                if (id == null || requestDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var request = Db.Requests.Find(id);
                request.DateModified = DateTime.Now;
                request.ShopId = requestDTO.ShopId;
                request.ActualFinishDate = requestDTO.ActualFinishDate;
                request.ActualPrice = requestDTO.ActualPrice;
                request.BillPicture = requestDTO.BillPicture;
                request.EstimatedFinishDate = requestDTO.EstimatedFinishDate;
                request.EstimatedPrice = requestDTO.EstimatedPrice;
                request.Price = requestDTO.Price;
                request.UserId = requestDTO.UserId;

                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

    }
}