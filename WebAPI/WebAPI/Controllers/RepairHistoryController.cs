using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class RepairHistoryController : BaseController
    {
        public RepairHistoryController() : base(nameof(RepairHistoryController)) { }

        [HttpGet]
        [Route("api/RepairHistory/{id}")]
        public BaseResponse RetrieveRepairHistory(string id)
        {
            try
            {
                var repairHistory = Db.RepairHistories.Find(id);
                if (repairHistory == null || repairHistory.Deleted)
                {
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(repairHistory.ToDTO());
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
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(RepairHistory.ToListDTO(repairHistory));
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpDelete]
        [Route("api/RepairHistory/{id}")]
        public BaseResponse DeleteRepairHistory(string id)
        {
            try
            {
                var repairHistory = Db.RepairHistories.Find(id);
                if (repairHistory == null || repairHistory.Deleted)
                {
                    throw new Exception("Record not found");
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
        public BaseResponse UpdateRepairHistory([FromUri] string id, [FromBody] RepairHistoryDTO repairHistoryDTO)
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
                    throw new Exception("Record not found");
                }
                repairHistory.DateModified = DateTime.Now;
                repairHistory.UserId = repairHistoryDTO.UserId;
                repairHistory.CarId = repairHistoryDTO.CarId;
                repairHistory.DateOfRepair = repairHistoryDTO.DateOfRepair;
                repairHistory.ShopId = repairHistoryDTO.ShopId;

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