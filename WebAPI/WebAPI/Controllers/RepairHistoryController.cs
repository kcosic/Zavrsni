﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.Exceptions;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class RepairHistoryController : BaseController
    {
        public RepairHistoryController() : base(nameof(RepairHistoryController)) { }

        [HttpGet]
        [Route("api/RepairHistory/{id}")]
        public BaseResponse RetrieveRepairHistory(int id, bool expanded = false)
        {
            try
            {
                var repairHistory = Db.RepairHistories.Find(id);
                if (repairHistory == null || repairHistory.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(repairHistory.ToDTO(!expanded));
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
        [Route("api/RepairHistory")]
        public BaseResponse RetrieveRepairHistorys()
        {
            try
            {
                var repairHistory = Db.RepairHistories.Where(x => x.UserId == AuthUser.Id && !x.Deleted).ToList();
                if (repairHistory == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(RepairHistory.ToListDTO(repairHistory));
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
        [Route("api/RepairHistory/{id}")]
        public BaseResponse DeleteRepairHistory(int id)
        {
            try
            {
                var repairHistory = Db.RepairHistories.Find(id);
                if (repairHistory == null || repairHistory.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                repairHistory.DateDeleted = DateTime.Now;
                repairHistory.Deleted = true;
                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPost]
        [Route("api/RepairHistory")]
        public BaseResponse CreateRepairHistory(RepairHistoryDTO newRepairHistoryDTO)
        {
            try
            {
                if (newRepairHistoryDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var newRepairHistory = new RepairHistory
                {
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    UserId = newRepairHistoryDTO.UserId,
                    CarId= newRepairHistoryDTO.CarId,
                    DateOfRepair = newRepairHistoryDTO.DateOfRepair,
                    ShopId = newRepairHistoryDTO.ShopId                                        
                };

                Db.RepairHistories.Add(newRepairHistory);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/RepairHistory/{id}")]
        public BaseResponse UpdateRepairHistory([FromUri] int id, [FromBody] RepairHistoryDTO repairHistoryDTO)
        {
            try
            {
                if (id == null || repairHistoryDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var repairHistory = Db.RepairHistories.Find(id);
                if (repairHistory == null || repairHistory.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                repairHistory.DateModified = DateTime.Now;
                repairHistory.UserId = repairHistoryDTO.UserId;
                repairHistory.CarId = repairHistoryDTO.CarId;
                repairHistory.DateOfRepair = repairHistoryDTO.DateOfRepair;
                repairHistory.ShopId = repairHistoryDTO.ShopId;

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