using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Appointment { 
        public static AppointmentDTO ToDTO(Appointment appointment, bool singleLevel = true)
        {
            return new AppointmentDTO
            {
                Date = appointment.Date,
                Shop = singleLevel ? null : Shop.ToDTO(appointment.Shop),
                DateCreated = appointment.DateCreated,
                DateDeleted = appointment.DateDeleted,
                DateModified = appointment.DateModified,
                Deleted = appointment.Deleted,
                Id = appointment.Id,
                IsTaken = appointment.IsTaken,
                ShopId = appointment.ShopId
            };
        }

        public static ICollection<AppointmentDTO> ToListDTO(ICollection<Appointment> list, bool singleLevel = true)
        {
            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public AppointmentDTO ToDTO(bool singleLevel = true)
        {
            return new AppointmentDTO
            {
                Date = Date,
                Shop = singleLevel ? null : Shop.ToDTO(Shop),
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                IsTaken = IsTaken,
                ShopId = ShopId
            };
        }
    }

}