using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.Helpers;
using WebAPI.Models.HERE;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class AppointmentController : BaseController
    {
        public AppointmentController() : base(nameof(AppointmentController)) { }

        [HttpGet]
        [Route("api/Appointment/{id}")]
        public BaseResponse RetrieveAppointment(string id)
        {
            try
            {
                var appointment = Db.Appointments.Find(id);
                if(appointment == null || appointment.Deleted)
                {
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(appointment.ToDTO());
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }        
        
        [HttpGet]
        [Route("api/Appointment")]
        public BaseResponse RetrieveAppointments()
        {
            try
            {
                var appointment = Db.Appointments.Where(x=> x.ShopId == AuthShop.Id && !x.Deleted).ToList();
                if(appointment == null)
                {
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(Appointment.ToListDTO(appointment));
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }        

        [HttpDelete]
        [Route("api/Appointment/{id}")]
        public BaseResponse DeleteAppointment(string id)
        {
            try
            {
                var appointment = Db.Appointments.Find(id);
                if (appointment == null || appointment.Deleted)
                {
                    throw new Exception("Record not found");
                }
                appointment.DateDeleted = DateTime.Now;
                appointment.Deleted = true;
                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPost]
        [Route("api/Appointment")]
        public BaseResponse CreateAppointment(AppointmentDTO newAppointmentDTO)
        {
            try
            {
                if (newAppointmentDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var newAppointment = new Appointment
                {
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    Date = newAppointmentDTO.Date,
                    IsTaken = newAppointmentDTO.IsTaken,
                    ShopId = newAppointmentDTO.ShopId
                };

                Db.Appointments.Add(newAppointment);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/Appointment/{id}")]
        public BaseResponse UpdateAppointment([FromUri]string id, [FromBody] AppointmentDTO appointmentDTO)
        {
            try
            {
                if (id == null || appointmentDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var appointment = Db.Appointments.Find(id);

                appointment.Date = appointmentDTO.Date;
                appointment.DateModified = DateTime.Now;
                appointment.IsTaken = appointmentDTO.IsTaken;
                appointment.ShopId= appointmentDTO.ShopId;

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