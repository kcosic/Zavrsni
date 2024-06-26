﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.Exceptions;
using WebAPI.Models.ORM;
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
                var requestDTO = request.ToDTO(expanded ? 3 : 2);
                //if (expanded)
                //{

                //    requestDTO.Shop.Location = request.Shop.Location.ToDTO();
                //    requestDTO.User = request.User.ToDTO();
                //}

                return CreateOkResponse(requestDTO);
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

                return CreateOkResponse(Models.ORM.Request.ToListDTO(request, 2));
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
        [Route("api/Request/Current")]
        public BaseResponse RetrieveCurrentShopRequest()
        {
            try
            {
                var requests = Db.Requests.Where(x => 
                        x.RepairDate.HasValue && 
                        !x.Completed && 
                        x.ShopAccepted.HasValue && 
                        x.UserAccepted.HasValue &&
                        x.ShopAccepted.Value && x.UserAccepted.Value &&
                        !x.Deleted)
                    
                    .ToList();

                var request = requests.Where(x =>  x.RepairDate.Value.Date <= DateTime.Now.Date).OrderByDescending(x => x.RepairDate.Value.Date).FirstOrDefault();
                if (request == null)
                {
                    throw new RecordNotFoundException("No current requests found");
                }

                return CreateOkResponse(Models.ORM.Request.ToDTO(request, 3));
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
        [Route("api/Request/Next")]
        public BaseResponse RetrieveNextShopRequest()
        {
            try
            {
                var requests = Db.Requests.Where(x => 
                        x.RepairDate.HasValue && 
                        !x.Completed &&
                        x.ShopAccepted.HasValue &&
                        x.UserAccepted.HasValue &&
                        x.ShopAccepted.Value && x.UserAccepted.Value &&
                        !x.Deleted)
                    .ToList();

                var request = requests.Where(x => x.RepairDate.Value.Date > DateTime.Now.Date).OrderByDescending(x => x.RepairDate.Value.Date).FirstOrDefault();

                if (request == null)
                {
                    throw new RecordNotFoundException("No upcoming requests found");
                }

                return CreateOkResponse(Models.ORM.Request.ToDTO(request, 3));
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
                if (newRequestDTO == null)
                {
                    throw new ArgumentNullException();
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
                    CarId = newRequestDTO.CarId,
                    ShopAccepted = newRequestDTO.ShopAccepted,
                    UserAccepted = newRequestDTO.UserAccepted,
                    IssueDescription = newRequestDTO.IssueDescription,
                    RepairDate = newRequestDTO.RepairDate,
                    RequestDate = newRequestDTO.RequestDate,
                    Completed = newRequestDTO.Completed
                };

                Db.Requests.Add(newRequest);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (ArgumentNullException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.InvalidParameter);
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
                request.ShopAccepted = requestDTO.ShopAccepted;
                request.ShopAcceptedDate = requestDTO.ShopAcceptedDate;
                request.UserAccepted = requestDTO.UserAccepted;
                request.UserAcceptedDate = requestDTO.UserAcceptedDate;
                request.IssueDescription = requestDTO.IssueDescription;
                request.RepairDate = requestDTO.RepairDate;
                request.RequestDate = requestDTO.RequestDate;
                request.Completed = requestDTO.Completed;

                Db.Issues.RemoveRange(request.Issues);
                request.Issues = requestDTO.Issues.Select(x=> {
                    return new Issue {
                        Accepted = x.Accepted,
                        DateCreated = x.DateCreated,
                        DateDeleted = x.DateDeleted,
                        Deleted = x.Deleted,
                        RequestId = x.RequestId,
                        DateModified = x.DateModified,
                        Price = x.Price,
                        Description = x.Description
                    };
                }).ToList();


                Db.SaveChanges();

                return CreateOkResponse(request.ToDTO(2));
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

                var activeRequest = user.Requests.Where(x => 

                x.RepairDate.HasValue && 
                (x.RepairDate.Value.Date >= DateTime.Now.Date || 
                x.RepairDate.Value.Date <= DateTime.Now.Date.AddDays(30))
                     && 
                !x.Completed && 
                !x.Deleted)?.OrderBy(x => x.RepairDate)?.FirstOrDefault();
                if (activeRequest == null || activeRequest.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var activeRequestDTO = activeRequest.ToDTO(3);

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

                return CreateOkResponse(Models.ORM.Request.ToListDTO(activeRequests, 2));
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