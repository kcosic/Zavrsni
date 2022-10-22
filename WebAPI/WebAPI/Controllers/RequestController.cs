using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.Exceptions;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class RequestController : BaseController
    {
        public RequestController() : base(nameof(RequestController)) { }

        [HttpGet]
        [Route("api/Request/{id}")]
        public BaseResponse RetrieveRequest(int id, bool expanded = false)
        {
            try
            {
                var request = Db.Requests.Find(id);
                if (request == null || request.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(request.ToDTO(!expanded));
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
        [Route("api/Request")]
        public BaseResponse RetrieveRequests()
        {
            try
            {
                var request = Db.Requests.Where(x => x.ShopId == AuthShop.Id && !x.Deleted).ToList();
                if (request == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Models.ORM.Request.ToListDTO(request));
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
        [Route("api/Request/{id}")]
        public BaseResponse DeleteRequest(int id)
        {
            try
            {
                var request = Db.Requests.Find(id);
                if (request == null || request.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                request.DateDeleted = DateTime.Now;
                request.Deleted = true;

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
                    FinishDate = newRequestDTO.FinishDate,
                    BillPicture= newRequestDTO.BillPicture,
                    EstimatedRepairHours= newRequestDTO.EstimatedRepairHours,
                    EstimatedPrice= newRequestDTO.EstimatedPrice,
                    Price= newRequestDTO.Price,
                    UserId= newRequestDTO.UserId,
                    CarId = newRequestDTO.CarId
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
        public BaseResponse UpdateRequest([FromUri] int id, [FromBody] Models.ORM.Request requestDTO)
        {
            try
            {
                if (requestDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var request = Db.Requests.Find(id);
                if (request == null || request.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                request.DateModified = DateTime.Now;
                request.ShopId = requestDTO.ShopId;
                request.FinishDate = requestDTO.FinishDate;
                request.BillPicture = requestDTO.BillPicture;
                request.EstimatedRepairHours = requestDTO.EstimatedRepairHours;
                request.EstimatedPrice = requestDTO.EstimatedPrice;
                request.Price = requestDTO.Price;
                request.UserId = requestDTO.UserId;
                request.CarId = requestDTO.CarId;

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
        [Route("api/Request/User/{id}/Active")]
        public BaseResponse RetrieveActiveUserRequest(int id)
        {
            try
            {
                var user = Db.Users.Find(id);
                if (user == null || user.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var activeRequest = user.Requests.Where(x => !x.Completed && !x.Deleted)?.OrderBy(x => x.DateCreated)?.FirstOrDefault();
                if (activeRequest == null || activeRequest.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var activeRequestDTO = activeRequest.ToDTO(false);
                activeRequestDTO.Shop.Location = activeRequest.Shop.Location.ToDTO();
                activeRequestDTO.User = user.ToDTO(false);

                return CreateOkResponse(activeRequestDTO);
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
        [Route("api/Request/User/{id}")]
        public BaseResponse RetrieveUserRequests(int id)
        {
            try
            {
                var user = Db.Users.Find(id);
                if (user == null || user.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var activeRequests = user.Requests.Where(x => !x.Completed && !x.Deleted)?.OrderBy(x => x.DateCreated)?.ToList();
                if (activeRequests == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Models.ORM.Request.ToListDTO(activeRequests));
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